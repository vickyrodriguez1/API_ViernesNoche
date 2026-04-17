package com.uade.tpo.e_commerce3.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.e_commerce3.dto.ActualizarCantidadRequest;
import com.uade.tpo.e_commerce3.dto.AgregarProductoRequest;
import com.uade.tpo.e_commerce3.dto.CarritoRequestDTO;
import com.uade.tpo.e_commerce3.dto.CarritoResponseDTO;
import com.uade.tpo.e_commerce3.service.CarritoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/carritos")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    // GET - obtener carrito por id
    // http://localhost:8080/api/carritos/1
    @GetMapping("/{id}")
    public ResponseEntity<CarritoResponseDTO> getCarritoById(@PathVariable Long id) {
        return ResponseEntity.ok(carritoService.getCarritoById(id));
    }

    // GET - obtener carrito del usuario (alternativa)
    // http://localhost:8080/api/carritos/user/1
    @GetMapping("/user/{usuarioId}")
    public ResponseEntity<CarritoResponseDTO> getCarritoPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(carritoService.getCarritoPorUsuario(usuarioId));
    }

    // POST - crear nuevo carrito
    // http://localhost:8080/api/carritos
    @PostMapping
    public ResponseEntity<CarritoResponseDTO> crearCarrito(@Valid @RequestBody CarritoRequestDTO request) {
        CarritoResponseDTO created = carritoService.crearCarrito(request.getUsuarioId());
        return ResponseEntity.created(URI.create("/api/carritos/" + created.getId())).body(created);
    }

    // POST - agregar producto al carrito
    // http://localhost:8080/api/carritos/1/productos
    @PostMapping("/{carritoId}/productos")
    public ResponseEntity<CarritoResponseDTO> agregarProducto(@PathVariable Long carritoId,
            @Valid @RequestBody AgregarProductoRequest request) {
        return ResponseEntity.ok(carritoService.agregarProducto(carritoId, request.getProductoId(), request.getCantidad()));
    }

    // PUT - actualizar cantidad de producto en el carrito
    // http://localhost:8080/api/carritos/1/items/1
    @PutMapping("/{carritoId}/items/{itemId}")
    public ResponseEntity<CarritoResponseDTO> actualizarCantidad(@PathVariable Long carritoId,
            @PathVariable Long itemId,
            @Valid @RequestBody ActualizarCantidadRequest request) {
        return ResponseEntity.ok(carritoService.actualizarCantidad(carritoId, itemId, request.getCantidad()));
    }

    // DELETE - eliminar producto del carrito
    // http://localhost:8080/api/carritos/1/items/1
    @DeleteMapping("/{carritoId}/items/{itemId}")
    public ResponseEntity<CarritoResponseDTO> eliminarProducto(@PathVariable Long carritoId, @PathVariable Long itemId) {
        return ResponseEntity.ok(carritoService.eliminarProducto(carritoId, itemId));
    }

    // DELETE - vaciar carrito
    // http://localhost:8080/api/carritos/1/vaciar
    @DeleteMapping("/{carritoId}/vaciar")
    public ResponseEntity<CarritoResponseDTO> vaciarCarrito(@PathVariable Long carritoId) {
        return ResponseEntity.ok(carritoService.vaciarCarrito(carritoId));
    }

    // GET - obtener total del carrito
    // http://localhost:8080/api/carritos/1/total
    @GetMapping("/{carritoId}/total")
    public ResponseEntity<Double> calcularTotal(@PathVariable Long carritoId) {
        return ResponseEntity.ok(carritoService.calcularTotal(carritoId));
    }

    // PUT - completar carrito
    // http://localhost:8080/api/carritos/1/completar
    @PutMapping("/{carritoId}/completar")
    public ResponseEntity<CarritoResponseDTO> completarCarrito(@PathVariable Long carritoId) {
        return ResponseEntity.ok(carritoService.completarCarrito(carritoId));
    }

    // DELETE - eliminar carrito
    // http://localhost:8080/api/carritos/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCarrito(@PathVariable Long id) {
        carritoService.deleteCarrito(id);
        return ResponseEntity.noContent().build();
    }
}

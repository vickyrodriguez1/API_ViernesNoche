package com.uade.tpo.e_commerce3.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.e_commerce3.dto.AgregarProductoRequest;
import com.uade.tpo.e_commerce3.dto.CarritoRequestDTO;
import com.uade.tpo.e_commerce3.dto.CarritoResponseDTO;
import com.uade.tpo.e_commerce3.dto.PedidoResponseDTO;
import com.uade.tpo.e_commerce3.service.CarritoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/carritos")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    // GET - obtener carrito del usuario (crea uno si no existe)
    // http://localhost:8080/api/carritos/usuario/1
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<CarritoResponseDTO> obtenerCarritoDelUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(carritoService.obtenerCarritoDelUsuario(usuarioId));
    }

    // GET - obtener carrito por id
    // http://localhost:8080/api/carritos/1
    @GetMapping("/{id}")
    public ResponseEntity<CarritoResponseDTO> getCarritoById(@PathVariable Long id) {
        return ResponseEntity.ok(carritoService.getCarritoById(id));
    }

    // GET - obtener carrito del usuario por usuarioId
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
        return ResponseEntity.ok(carritoService.agregarProducto(carritoId, request.getProductoId()));
    }

    // DELETE - eliminar producto del carrito
    // http://localhost:8080/api/carritos/1/productos/2
    @DeleteMapping("/{carritoId}/productos/{productoId}")
    public ResponseEntity<CarritoResponseDTO> eliminarProducto(@PathVariable Long carritoId,
            @PathVariable Long productoId) {
        return ResponseEntity.ok(carritoService.eliminarProducto(carritoId, productoId));
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

    // POST - pagar carrito: crea el pedido y limpia productos_carrito
    // http://localhost:8080/api/carritos/1/pagar
    @PostMapping("/{carritoId}/pagar")
    public ResponseEntity<PedidoResponseDTO> pagar(@PathVariable Long carritoId) {
        return ResponseEntity.ok(carritoService.pagar(carritoId));
    }

    // DELETE - eliminar carrito
    // http://localhost:8080/api/carritos/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCarrito(@PathVariable Long id) {
        carritoService.deleteCarrito(id);
        return ResponseEntity.noContent().build();
    }
}

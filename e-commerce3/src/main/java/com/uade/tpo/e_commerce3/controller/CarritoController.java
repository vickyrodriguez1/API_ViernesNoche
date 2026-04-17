package com.uade.tpo.e_commerce3.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.uade.tpo.e_commerce3.dto.CarritoRequest;
import com.uade.tpo.e_commerce3.model.Carrito;
import com.uade.tpo.e_commerce3.service.CarritoService;

@RestController
@RequestMapping("/api/carritos")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    // GET - obtener carrito del usuario
    // http://localhost:8080/api/carritos/usuario/1
    @GetMapping("/usuario/{usuarioId}")
    public Carrito obtenerCarritoDelUsuario(@PathVariable Long usuarioId) {
        return carritoService.obtenerCarritoDelUsuario(usuarioId);
    }

    // GET - obtener carrito por id
    // http://localhost:8080/api/carritos/1
    @GetMapping("/{id}")
    public Carrito getCarritoById(@PathVariable Long id) {
        return carritoService.getCarritoById(id);
    }

    // GET - obtener carrito del usuario (alternativa)
    // http://localhost:8080/api/carritos/user/1
    @GetMapping("/user/{usuarioId}")
    public Carrito getCarritoPorUsuario(@PathVariable Long usuarioId) {
        return carritoService.getCarritoPorUsuario(usuarioId);
    }

    // POST - crear nuevo carrito
    // http://localhost:8080/api/carritos
    @PostMapping
    public Carrito crearCarrito(@RequestBody CarritoRequest request) {
        return carritoService.crearCarrito(request.getUsuarioId());
    }

    // POST - agregar producto al carrito
    // http://localhost:8080/api/carritos/1/productos
    @PostMapping("/{carritoId}/productos")
    public Carrito agregarProducto(@PathVariable Long carritoId, @RequestBody AgregarProductoRequest request) {
        return carritoService.agregarProducto(carritoId, request.getProductoId(), request.getCantidad());
    }

    // PUT - actualizar cantidad de producto en el carrito
    // http://localhost:8080/api/carritos/1/items/1
    @PutMapping("/{carritoId}/items/{itemId}")
    public Carrito actualizarCantidad(@PathVariable Long carritoId, @PathVariable Long itemId,
            @RequestBody ActualizarCantidadRequest request) {
        return carritoService.actualizarCantidad(carritoId, itemId, request.getCantidad());
    }

    // DELETE - eliminar producto del carrito
    // http://localhost:8080/api/carritos/1/items/1
    @DeleteMapping("/{carritoId}/items/{itemId}")
    public Carrito eliminarProducto(@PathVariable Long carritoId, @PathVariable Long itemId) {
        return carritoService.eliminarProducto(carritoId, itemId);
    }

    // DELETE - vaciar carrito
    // http://localhost:8080/api/carritos/1/vaciar
    @DeleteMapping("/{carritoId}/vaciar")
    public Carrito vaciarCarrito(@PathVariable Long carritoId) {
        return carritoService.vaciarCarrito(carritoId);
    }

    // GET - obtener total del carrito
    // http://localhost:8080/api/carritos/1/total
    @GetMapping("/{carritoId}/total")
    public Double calcularTotal(@PathVariable Long carritoId) {
        return carritoService.calcularTotal(carritoId);
    }

    // PUT - completar carrito
    // http://localhost:8080/api/carritos/1/completar
    @PutMapping("/{carritoId}/completar")
    public Carrito completarCarrito(@PathVariable Long carritoId) {
        return carritoService.completarCarrito(carritoId);
    }

    // DELETE - eliminar carrito
    // http://localhost:8080/api/carritos/1
    @DeleteMapping("/{id}")
    public void deleteCarrito(@PathVariable Long id) {
        carritoService.deleteCarrito(id);
    }
}

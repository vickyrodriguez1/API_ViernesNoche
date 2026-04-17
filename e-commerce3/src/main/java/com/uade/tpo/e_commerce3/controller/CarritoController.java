package com.uade.tpo.e_commerce3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.e_commerce3.dto.AgregarProductoRequest;
import com.uade.tpo.e_commerce3.dto.CarritoRequest;
import com.uade.tpo.e_commerce3.model.Carrito;
import com.uade.tpo.e_commerce3.model.Pedido;
import com.uade.tpo.e_commerce3.service.CarritoService;

@RestController
@RequestMapping("/api/carritos")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    // GET - obtener carrito del usuario (crea uno si no existe)
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

    // GET - obtener carrito del usuario por usuarioId
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
        return carritoService.agregarProducto(carritoId, request.getProductoId());
    }

    // DELETE - eliminar producto del carrito
    // http://localhost:8080/api/carritos/1/productos/2
    @DeleteMapping("/{carritoId}/productos/{productoId}")
    public Carrito eliminarProducto(@PathVariable Long carritoId, @PathVariable Long productoId) {
        return carritoService.eliminarProducto(carritoId, productoId);
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

    // POST - pagar carrito: crea el pedido y limpia productos_carrito
    // http://localhost:8080/api/carritos/1/pagar
    @PostMapping("/{carritoId}/pagar")
    public Pedido pagar(@PathVariable Long carritoId) {
        return carritoService.pagar(carritoId);
    }

    // DELETE - eliminar carrito
    // http://localhost:8080/api/carritos/1
    @DeleteMapping("/{id}")
    public void deleteCarrito(@PathVariable Long id) {
        carritoService.deleteCarrito(id);
    }
}

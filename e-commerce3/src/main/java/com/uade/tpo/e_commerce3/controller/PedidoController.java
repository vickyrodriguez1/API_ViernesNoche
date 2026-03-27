package com.uade.tpo.e_commerce3.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.e_commerce3.model.Pedido;
import com.uade.tpo.e_commerce3.model.Usuario;
import com.uade.tpo.e_commerce3.service.PedidoService;
import com.uade.tpo.e_commerce3.service.UsuarioService;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private UsuarioService usuarioService;

    // GET all pedidos
    // http://localhost:8080/api/pedidos
    @GetMapping
    public List<Pedido> getAllPedidos() {
        return pedidoService.getAllPedidos();
    }

    // GET pedido by id
    // http://localhost:8080/api/pedidos/1
    @GetMapping("/{id}")
    public Pedido getPedidoById(@PathVariable Long id) {
        return pedidoService.getPedidoById(id);
    }

    // GET pedidos por usuario
    // http://localhost:8080/api/pedidos/usuario/1
    @GetMapping("/usuario/{usuarioId}")
    public List<Pedido> getPedidosByUsuario(@PathVariable Long usuarioId) {
        Usuario usuario = usuarioService.getUsuarioById(usuarioId);
        if (usuario != null) {
            return pedidoService.getPedidosByUsuario(usuario);
        }
        return null;
    }

    // POST - crear nuevo pedido
    // http://localhost:8080/api/pedidos
    @PostMapping
    public Pedido savePedido(@RequestBody Pedido pedido) {
        return pedidoService.savePedido(pedido);
    }

    // PUT - actualizar pedido
    // http://localhost:8080/api/pedidos/1
    @PutMapping("/{id}")
    public Pedido updatePedido(@PathVariable Long id, @RequestBody Pedido pedido) {
        return pedidoService.updatePedido(id, pedido);
    }

    // DELETE - eliminar pedido
    // http://localhost:8080/api/pedidos/1
    @DeleteMapping("/{id}")
    public void deletePedidoById(@PathVariable Long id) {
        pedidoService.deletePedidoById(id);
    }
}

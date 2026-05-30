package com.uade.tpo.e_commerce3.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.e_commerce3.dto.PedidoResponseDTO;
import com.uade.tpo.e_commerce3.dto.PedidoUpdateDTO;
import com.uade.tpo.e_commerce3.service.PedidoService;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    // GET all pedidos
    // http://localhost:8080/api/pedidos
    @GetMapping
    public ResponseEntity<List<PedidoResponseDTO>> getAllPedidos() {
        return ResponseEntity.ok(pedidoService.getAllPedidos());
    }

    // GET pedido by id
    // http://localhost:8080/api/pedidos/1
    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> getPedidoById(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.getPedidoById(id));
    }

    // GET pedidos por usuario
    // http://localhost:8080/api/pedidos/usuario/1
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<PedidoResponseDTO>> getPedidosByUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(pedidoService.getPedidosByUsuario(usuarioId));
    }

    // PUT - actualizar estado y/o fecha de entrega del pedido
    // http://localhost:8080/api/pedidos/1
    @PutMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> updatePedido(@PathVariable Long id,
            @RequestBody PedidoUpdateDTO dto) {
        return ResponseEntity.ok(pedidoService.updatePedido(id, dto));
    }

    // DELETE - eliminar pedido
    // http://localhost:8080/api/pedidos/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePedidoById(@PathVariable Long id) {
        pedidoService.deletePedidoById(id);
        return ResponseEntity.noContent().build();
    }
}

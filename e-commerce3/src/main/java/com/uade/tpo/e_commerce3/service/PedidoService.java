package com.uade.tpo.e_commerce3.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.e_commerce3.model.Pedido;
import com.uade.tpo.e_commerce3.model.Usuario;
import com.uade.tpo.e_commerce3.repository.PedidoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PedidoService {
    
    @Autowired
    private PedidoRepository pedidoRepository;
    
    public List<Pedido> getAllPedidos() {
        return pedidoRepository.findAll();
    }

    public Pedido getPedidoById(Long id) {
        return pedidoRepository.findById(id).orElse(null);
    }

    public List<Pedido> getPedidosByUsuario(Usuario usuario) {
        return pedidoRepository.findByUsuario(usuario);
    }

    public void deletePedidoById(Long id) {
        pedidoRepository.deleteById(id);
    }

    public Pedido savePedido(Pedido pedido) {
        if (pedido.getFechaPedido() == null) {
            pedido.setFechaPedido(LocalDateTime.now());
        }
        if (pedido.getEstado() == null) {
            pedido.setEstado("pendiente");
        }
        return pedidoRepository.save(pedido);
    }

    public Pedido updatePedido(Long id, Pedido pedido) {
        Pedido existingPedido = getPedidoById(id);
        if (existingPedido != null) {
            existingPedido.setUsuario(pedido.getUsuario());
            existingPedido.setProducto(pedido.getProducto());
            existingPedido.setCantidad(pedido.getCantidad());
            existingPedido.setPrecioTotal(pedido.getPrecioTotal());
            existingPedido.setEstado(pedido.getEstado());
            existingPedido.setFechaEntrega(pedido.getFechaEntrega());
            return pedidoRepository.save(existingPedido);
        }
        return null;
    }
}

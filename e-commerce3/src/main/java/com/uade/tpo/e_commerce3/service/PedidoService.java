package com.uade.tpo.e_commerce3.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.e_commerce3.exception.ResourceNotFoundException;
import com.uade.tpo.e_commerce3.model.Pedido;
import com.uade.tpo.e_commerce3.model.Usuario;
import com.uade.tpo.e_commerce3.repository.PedidoRepository;
import com.uade.tpo.e_commerce3.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Pedido> getAllPedidos() {
        return pedidoRepository.findAll();
    }

    public Pedido getPedidoById(Long id) {
        return getPedidoEntityById(id);
    }

    public List<Pedido> getPedidosByUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + usuarioId));
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
        Pedido existingPedido = getPedidoEntityById(id);
        existingPedido.setUsuario(pedido.getUsuario());
        existingPedido.setProductos(pedido.getProductos());
        existingPedido.setPrecioTotal(pedido.getPrecioTotal());
        existingPedido.setEstado(pedido.getEstado());
        existingPedido.setFechaEntrega(pedido.getFechaEntrega());
        return pedidoRepository.save(existingPedido);
    }

    private Pedido getPedidoEntityById(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con id: " + id));
    }
}

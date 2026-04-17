package com.uade.tpo.e_commerce3.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.e_commerce3.dto.PedidoRequestDTO;
import com.uade.tpo.e_commerce3.dto.PedidoResponseDTO;
import com.uade.tpo.e_commerce3.exception.ResourceNotFoundException;
import com.uade.tpo.e_commerce3.model.Pedido;
import com.uade.tpo.e_commerce3.model.Producto;
import com.uade.tpo.e_commerce3.model.Usuario;
import com.uade.tpo.e_commerce3.repository.PedidoRepository;
import com.uade.tpo.e_commerce3.repository.ProductoRepository;
import com.uade.tpo.e_commerce3.repository.UsuarioRepository;
import com.uade.tpo.e_commerce3.service.mapper.PedidoMapper;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PedidoService {
    
    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private PedidoMapper pedidoMapper;

    public List<PedidoResponseDTO> getAllPedidos() {
        return pedidoRepository.findAll().stream()
                .map(pedidoMapper::toDto)
                .collect(Collectors.toList());
    }

    public PedidoResponseDTO getPedidoById(Long id) {
        return pedidoMapper.toDto(getPedidoEntityById(id));
    }

    public List<PedidoResponseDTO> getPedidosByUsuario(Long usuarioId) {
        Usuario usuario = usuarioService.getUsuarioEntityById(usuarioId);
        return pedidoRepository.findByUsuario(usuario).stream()
                .map(pedidoMapper::toDto)
                .collect(Collectors.toList());
    }

    public void deletePedidoById(Long id) {
        pedidoRepository.deleteById(id);
    }

    public PedidoResponseDTO savePedido(PedidoRequestDTO request) {
        Usuario usuario = usuarioService.getUsuarioEntityById(request.getUsuarioId());;

        Producto producto = productoService.getProductoEntityById(request.getProductoId());

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setProducto(producto);
        pedido.setCantidad(request.getCantidad());
        pedido.setPrecioTotal(producto.getPrecio() * request.getCantidad());
        pedido.setFechaPedido(LocalDateTime.now());
        pedido.setEstado(request.getEstado() != null ? request.getEstado() : "pendiente");
        if (request.getFechaEntrega() != null) {
            pedido.setFechaEntrega(request.getFechaEntrega());
        }

        return pedidoMapper.toDto(pedidoRepository.save(pedido));
    }

    public PedidoResponseDTO updatePedido(Long id, PedidoRequestDTO request) {
        Pedido existingPedido = getPedidoEntityById(id);

        if (!existingPedido.getProducto().getId().equals(request.getProductoId())) {
            Producto nuevoProducto = productoService.getProductoEntityById(request.getProductoId());
            existingPedido.setProducto(nuevoProducto);
        }

        if (!existingPedido.getUsuario().getId().equals(request.getUsuarioId())) {
             Usuario nuevoUsuario = usuarioService.getUsuarioEntityById(request.getUsuarioId());;
             existingPedido.setUsuario(nuevoUsuario);
        }

        existingPedido.setCantidad(request.getCantidad());
        existingPedido.setPrecioTotal(existingPedido.getProducto().getPrecio() * request.getCantidad());

        if (request.getEstado() != null) {
            existingPedido.setEstado(request.getEstado());
        }
        if (request.getFechaEntrega() != null) {
            existingPedido.setFechaEntrega(request.getFechaEntrega());
        }

        return pedidoMapper.toDto(pedidoRepository.save(existingPedido));
    }

    private Pedido getPedidoEntityById(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con id: " + id));
    }
}

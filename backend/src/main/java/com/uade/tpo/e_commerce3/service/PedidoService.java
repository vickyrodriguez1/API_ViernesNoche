package com.uade.tpo.e_commerce3.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.e_commerce3.dto.PedidoResponseDTO;
import com.uade.tpo.e_commerce3.dto.PedidoUpdateDTO;
import com.uade.tpo.e_commerce3.exception.ResourceNotFoundException;
import com.uade.tpo.e_commerce3.model.Pedido;
import com.uade.tpo.e_commerce3.model.Usuario;
import com.uade.tpo.e_commerce3.repository.PedidoRepository;
import com.uade.tpo.e_commerce3.repository.UsuarioRepository;
import com.uade.tpo.e_commerce3.service.mapper.PedidoMapper;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

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
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + usuarioId));
        return pedidoRepository.findByUsuario(usuario).stream()
                .map(pedidoMapper::toDto)
                .collect(Collectors.toList());
    }

    public void deletePedidoById(Long id) {
        pedidoRepository.deleteById(id);
    }

    public PedidoResponseDTO updatePedido(Long id, PedidoUpdateDTO dto) {
        Pedido pedido = getPedidoEntityById(id);
        if (dto.getEstado() != null) {
            pedido.setEstado(dto.getEstado());
        }
        if (dto.getFechaEntrega() != null) {
            pedido.setFechaEntrega(dto.getFechaEntrega());
        }
        return pedidoMapper.toDto(pedidoRepository.save(pedido));
    }

    private Pedido getPedidoEntityById(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con id: " + id));
    }
}

package com.uade.tpo.e_commerce3.service.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.uade.tpo.e_commerce3.dto.PedidoResponseDTO;
import com.uade.tpo.e_commerce3.dto.ItemResponseDTO;
import com.uade.tpo.e_commerce3.model.Pedido;

@Component
public class PedidoMapper {

    private final ItemMapper itemMapper;

    public PedidoMapper(ItemMapper itemMapper) {
        this.itemMapper = itemMapper;
    }

    public PedidoResponseDTO toDto(Pedido pedido) {
        if (pedido == null) return null;

        List<ItemResponseDTO> items = pedido.getItems().stream()
                .map(itemMapper::toDto)
                .collect(Collectors.toList());

        Long usuarioId = pedido.getUsuario() != null ? pedido.getUsuario().getId() : null;

        return new PedidoResponseDTO(
                pedido.getId(),
                usuarioId,
                items,
                pedido.getPrecioTotal(),
                pedido.getFechaPedido(),
                pedido.getFechaEntrega(),
                pedido.getEstado());
    }
}

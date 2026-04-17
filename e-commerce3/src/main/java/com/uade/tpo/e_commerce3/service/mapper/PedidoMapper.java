package com.uade.tpo.e_commerce3.service.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.uade.tpo.e_commerce3.dto.PedidoResponseDTO;
import com.uade.tpo.e_commerce3.dto.ProductoListDTO;
import com.uade.tpo.e_commerce3.model.Pedido;

@Component
public class PedidoMapper {

    public PedidoResponseDTO toDto(Pedido pedido) {
        if (pedido == null) return null;

        List<ProductoListDTO> productos = pedido.getProductos().stream()
                .map(p -> {
                    ProductoListDTO dto = new ProductoListDTO();
                    dto.setId(p.getId());
                    dto.setNombre(p.getNombre());
                    dto.setPrecio(p.getPrecio());
                    return dto;
                })
                .collect(Collectors.toList());

        Long usuarioId = pedido.getUsuario() != null ? pedido.getUsuario().getId() : null;

        return new PedidoResponseDTO(
                pedido.getId(),
                usuarioId,
                productos,
                pedido.getPrecioTotal(),
                pedido.getFechaPedido(),
                pedido.getFechaEntrega(),
                pedido.getEstado());
    }
}

package com.uade.tpo.e_commerce3.service.mapper;

import org.springframework.stereotype.Component;

import com.uade.tpo.e_commerce3.dto.PedidoResponseDTO;
import com.uade.tpo.e_commerce3.model.Pedido;

@Component
public class PedidoMapper {

    public PedidoResponseDTO toDto(Pedido pedido) {
        if (pedido == null) {
            return null;
        }
        
        Long usuarioId = pedido.getUsuario() != null ? pedido.getUsuario().getId() : null;
        Long productoId = pedido.getProducto() != null ? pedido.getProducto().getId() : null;
        String productoNombre = pedido.getProducto() != null ? pedido.getProducto().getNombre() : null;

        return new PedidoResponseDTO(
                pedido.getId(),
                usuarioId,
                productoId,
                productoNombre,
                pedido.getCantidad(),
                pedido.getPrecioTotal(),
                pedido.getFechaPedido(),
                pedido.getFechaEntrega(),
                pedido.getEstado()
        );
    }
}

package com.uade.tpo.e_commerce3.service.mapper;

import org.springframework.stereotype.Component;

import com.uade.tpo.e_commerce3.dto.ItemResponseDTO;
import com.uade.tpo.e_commerce3.model.Item;

@Component
public class ItemMapper {

    public ItemResponseDTO toDto(Item item) {
        if (item == null) return null;

        Long productoId = item.getProducto() != null ? item.getProducto().getId() : null;
        String nombreProducto = item.getProducto() != null ? item.getProducto().getNombre() : null;
        Double subtotal = item.getCantidad() != null && item.getPrecioUnitario() != null
                ? item.getCantidad() * item.getPrecioUnitario()
                : null;

        return new ItemResponseDTO(
                item.getId(),
                productoId,
                nombreProducto,
                item.getCantidad(),
                item.getPrecioUnitario(),
                subtotal);
    }
}


package com.uade.tpo.e_commerce3.service.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.uade.tpo.e_commerce3.dto.CarritoResponseDTO;
import com.uade.tpo.e_commerce3.dto.ItemCarritoResponseDTO;
import com.uade.tpo.e_commerce3.model.Carrito;
import com.uade.tpo.e_commerce3.model.ItemCarrito;

@Component
public class CarritoMapper {

    public CarritoResponseDTO toDto(Carrito carrito) {
        if (carrito == null) {
            return null;
        }

        List<ItemCarritoResponseDTO> items = new ArrayList<>();
        double total = 0.0;

        if (carrito.getItems() != null) {
            for (ItemCarrito item : carrito.getItems()) {
                ItemCarritoResponseDTO itemDto = toItemDto(item);
                items.add(itemDto);
                total += itemDto.getSubtotal() != null ? itemDto.getSubtotal() : 0.0;
            }
        }

        return new CarritoResponseDTO(
                carrito.getFechaCreacion(),
                carrito.getFechaActualizacion(),
                items,
                total);
    }

    private ItemCarritoResponseDTO toItemDto(ItemCarrito item) {
        Double precio = item.getPrecioUnitario();
        int cantidad = item.getCantidad() != null ? item.getCantidad() : 0;
        double subtotal = precio != null ? precio * cantidad : 0.0;

        String productoNombre = item.getProducto() != null ? item.getProducto().getNombre() : null;

        return new ItemCarritoResponseDTO(
                subtotal,
                precio,
                cantidad,
                productoNombre);
    }
}

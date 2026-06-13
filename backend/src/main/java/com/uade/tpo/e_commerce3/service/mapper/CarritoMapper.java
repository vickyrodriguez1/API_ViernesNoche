package com.uade.tpo.e_commerce3.service.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.uade.tpo.e_commerce3.dto.CarritoResponseDTO;
import com.uade.tpo.e_commerce3.dto.ItemResponseDTO;
import com.uade.tpo.e_commerce3.model.Carrito;

@Component
public class CarritoMapper {

    private final ItemMapper itemMapper;

    public CarritoMapper(ItemMapper itemMapper) {
        this.itemMapper = itemMapper;
    }

    public CarritoResponseDTO toDto(Carrito carrito) {
        if (carrito == null) return null;

        List<ItemResponseDTO> items = carrito.getItems().stream()
                .map(itemMapper::toDto)
                .collect(Collectors.toList());

        Double total = carrito.getItems().stream()
                .mapToDouble(item -> item.getCantidad() * item.getPrecioUnitario())
                .sum();

        Long usuarioId = carrito.getUsuario() != null ? carrito.getUsuario().getId() : null;

        return new CarritoResponseDTO(
                carrito.getId(),
                usuarioId,
                carrito.getFechaCreacion(),
                carrito.getFechaActualizacion(),
                items,
                total);
    }
}

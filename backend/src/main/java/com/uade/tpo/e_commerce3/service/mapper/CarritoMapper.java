package com.uade.tpo.e_commerce3.service.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.uade.tpo.e_commerce3.dto.CarritoResponseDTO;
import com.uade.tpo.e_commerce3.dto.ProductoListDTO;
import com.uade.tpo.e_commerce3.model.Carrito;

@Component
public class CarritoMapper {

    public CarritoResponseDTO toDto(Carrito carrito) {
        if (carrito == null) return null;

        List<ProductoListDTO> productos = carrito.getProductos().stream()
                .map(p -> {
                    ProductoListDTO dto = new ProductoListDTO();
                    dto.setId(p.getId());
                    dto.setNombre(p.getNombre());
                    dto.setPrecio(p.getPrecio());
                    return dto;
                })
                .collect(Collectors.toList());

        Double total = carrito.getProductos().stream()
                .mapToDouble(p -> p.getPrecio())
                .sum();

        Long usuarioId = carrito.getUsuario() != null ? carrito.getUsuario().getId() : null;

        return new CarritoResponseDTO(
                carrito.getId(),
                usuarioId,
                carrito.getFechaCreacion(),
                carrito.getFechaActualizacion(),
                productos,
                total);
    }
}

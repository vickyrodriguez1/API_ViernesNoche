package com.uade.tpo.e_commerce3.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.uade.tpo.e_commerce3.model.Producto;

import lombok.Data;

@Data
public class ProductoResponseDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private Double precio;
    private Integer stock;
    private List<String> categorias;

    public static ProductoResponseDTO toDto(Producto producto) {
        ProductoResponseDTO dto = new ProductoResponseDTO();
        dto.setId(producto.getId());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setPrecio(producto.getPrecio());
        dto.setStock(producto.getStock());
        dto.setCategorias(
            producto.getCategorias().stream()
                .map(c -> c.getNombre())
                .collect(Collectors.toList())
        );
        return dto;
    }
}

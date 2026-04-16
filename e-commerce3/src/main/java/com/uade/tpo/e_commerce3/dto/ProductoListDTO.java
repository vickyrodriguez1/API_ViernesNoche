package com.uade.tpo.e_commerce3.dto;

import com.uade.tpo.e_commerce3.model.Producto;

import lombok.Data;

@Data
public class ProductoListDTO {

    private Long id;
    private String nombre;
    private Double precio;

    public static ProductoListDTO toDto(Producto producto) {
        ProductoListDTO dto = new ProductoListDTO();
        dto.setId(producto.getId());
        dto.setNombre(producto.getNombre());
        dto.setPrecio(producto.getPrecio());
        return dto;
    }
}

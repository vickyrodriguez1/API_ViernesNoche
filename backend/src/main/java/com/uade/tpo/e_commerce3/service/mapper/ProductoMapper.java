package com.uade.tpo.e_commerce3.service.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.uade.tpo.e_commerce3.dto.ProductoListDTO;
import com.uade.tpo.e_commerce3.dto.ProductoResponseDTO;
import com.uade.tpo.e_commerce3.model.Producto;

@Component
public class ProductoMapper {

    public ProductoResponseDTO toDto(Producto producto) {
        if (producto == null) return null;

        ProductoResponseDTO dto = new ProductoResponseDTO();
        dto.setId(producto.getId());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setPrecio(producto.getPrecio());
        dto.setStock(producto.getStock());
        
        List<String> categorias = null;
        if (producto.getCategorias() != null) {
            categorias = producto.getCategorias().stream()
                    .map(c -> c.getNombre())
                    .collect(Collectors.toList());
        }
        dto.setCategorias(categorias);
        
        return dto;
    }

    public ProductoListDTO toListDto(Producto producto) {
        if (producto == null) return null;

        ProductoListDTO dto = new ProductoListDTO();
        dto.setId(producto.getId());
        dto.setNombre(producto.getNombre());
        dto.setPrecio(producto.getPrecio());
        
        return dto;
    }
}


package com.uade.tpo.e_commerce3.service.mapper;

import org.springframework.stereotype.Component;

import com.uade.tpo.e_commerce3.dto.CategoriaResponseDTO;
import com.uade.tpo.e_commerce3.model.Categoria;

@Component
public class CategoriaMapper {

    public CategoriaResponseDTO toDto(Categoria categoria) {
        if (categoria == null) {
            return null;
        }
        return new CategoriaResponseDTO(
                categoria.getId(),
                categoria.getNombre()
        );
    }
}


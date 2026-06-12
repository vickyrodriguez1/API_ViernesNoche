package com.uade.tpo.e_commerce3.dto;

import java.util.List;

import lombok.Data;

@Data
public class ProductoResponseDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private Double precio;
    private Integer stock;
    private List<String> categorias;
}

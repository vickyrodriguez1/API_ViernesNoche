package com.uade.tpo.e_commerce3.dto;

import java.util.List;
import lombok.Data;

@Data
public class ProductoRequestDTO {
    private String nombre;
    private String descripcion;
    private Double precio;
    private Integer stock;
    private List<Long> categoriaIds;
}

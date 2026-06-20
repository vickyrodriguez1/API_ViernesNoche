package com.uade.tpo.e_commerce3.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProductoListDTO {

    private Long id;
    private String nombre;
    private Double precio;
    private String imagenBase64;
}

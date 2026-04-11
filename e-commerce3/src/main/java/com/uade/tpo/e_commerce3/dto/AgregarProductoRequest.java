package com.uade.tpo.e_commerce3.dto;

import lombok.Data;

@Data
public class AgregarProductoRequest {
    private Long productoId;
    private Integer cantidad;
}

package com.uade.tpo.e_commerce3.dto;

import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ItemCarritoResponseDTO {
    private Double subtotal;
    private Double precioUnitario;
    private Integer cantidad;
    private String productoNombre;
}

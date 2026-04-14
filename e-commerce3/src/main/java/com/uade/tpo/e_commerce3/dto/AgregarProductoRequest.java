package com.uade.tpo.e_commerce3.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AgregarProductoRequest {
    
    @NotNull(message = "El ID del producto es obligatorio")
    @Positive(message = "El ID del producto debe ser positivo")
    private Long productoId;
    
    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor a cero")
    @Max(value = 1000, message = "No podés agregar más de 1000 unidades")
    private Integer cantidad;
}

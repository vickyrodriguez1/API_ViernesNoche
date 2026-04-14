package com.uade.tpo.e_commerce3.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CarritoRequest {
    
    @NotNull(message = "El ID del usuario es obligatorio")
    @Positive(message = "El ID del usuario debe ser positivo")
    private Long usuarioId;
}

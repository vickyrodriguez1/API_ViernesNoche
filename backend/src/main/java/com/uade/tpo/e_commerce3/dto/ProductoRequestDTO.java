package com.uade.tpo.e_commerce3.dto;

import java.util.List;
import jakarta.validation.constraints.*; // Importamos todas las validaciones
import lombok.Data;

@Data
public class ProductoRequestDTO {

    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(min = 10, message = "La descripción debe tener al menos 10 caracteres")
    private String descripcion;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor a cero")
    private Double precio;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

    private List<Long> categoriaIds;
}
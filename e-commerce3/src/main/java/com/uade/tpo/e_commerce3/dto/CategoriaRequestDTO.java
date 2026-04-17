package com.uade.tpo.e_commerce3.dto;


import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class CategoriaRequestDTO {

    @NotBlank(message = "El nombre de la categoria no puede estar vacio")
    private String nombre;
}




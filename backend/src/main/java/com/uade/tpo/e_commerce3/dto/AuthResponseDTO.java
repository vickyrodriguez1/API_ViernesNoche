package com.uade.tpo.e_commerce3.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String rol;
}

package com.uade.tpo.e_commerce3.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UsuarioLoginDTO {
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Email inválido")
    private String email;
    
    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
}

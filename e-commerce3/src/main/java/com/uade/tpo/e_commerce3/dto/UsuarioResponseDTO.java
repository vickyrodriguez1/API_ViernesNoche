package com.uade.tpo.e_commerce3.dto;

import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.AllArgsConstructor;

import com.uade.tpo.e_commerce3.model.RolEnum;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UsuarioResponseDTO {
    private Long id;
    private String email;
    private String nombre;
    private String apellido;
    private String telefono;
    private String direccion;
    private RolEnum rol;
}

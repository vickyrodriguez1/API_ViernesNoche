package com.uade.tpo.e_commerce3.dto;

import lombok.Data;

@Data
public class UsuarioRegistroDTO {
    private String nombre;
    private String apellido;
    private String email;
    private String password;
    private String telefono;
    private String direccion;
    private String rol;
}

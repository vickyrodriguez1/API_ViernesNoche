package com.uade.tpo.e_commerce3.service.mapper;

import org.springframework.stereotype.Component;

import com.uade.tpo.e_commerce3.dto.UsuarioResponseDTO;
import com.uade.tpo.e_commerce3.model.Usuario;

@Component
public class UsuarioMapper {

    public UsuarioResponseDTO toDto(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getEmail(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getTelefono(),
                usuario.getDireccion(),
                usuario.getRol()
        );
    }
}

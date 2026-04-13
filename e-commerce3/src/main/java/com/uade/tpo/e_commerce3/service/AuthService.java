package com.uade.tpo.e_commerce3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.e_commerce3.dto.AuthResponseDTO;
import com.uade.tpo.e_commerce3.dto.UsuarioLoginDTO;
import com.uade.tpo.e_commerce3.dto.UsuarioRegistroDTO;
import com.uade.tpo.e_commerce3.exception.ArgumentInvalidException;
import com.uade.tpo.e_commerce3.exception.ResourceNotFoundException;
import com.uade.tpo.e_commerce3.model.RolEnum;
import com.uade.tpo.e_commerce3.model.Usuario;
import com.uade.tpo.e_commerce3.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public AuthResponseDTO registrar(UsuarioRegistroDTO dto) {
        if (usuarioRepository.findByEmail(dto.getEmail()) != null) {
            throw new ArgumentInvalidException("Ya existe un usuario con ese email");
        }

        RolEnum rol = RolEnum.CLIENTE;
        if (dto.getRol() != null && !dto.getRol().isBlank()) {
            try {
                rol = RolEnum.valueOf(dto.getRol().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new ArgumentInvalidException("Rol invalido. Valores permitidos: ADMIN, CLIENTE");
            }
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(dto.getPassword());
        usuario.setTelefono(dto.getTelefono());
        usuario.setDireccion(dto.getDireccion());
        usuario.setRol(rol);

        usuarioRepository.save(usuario);

        return new AuthResponseDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                rol.name());
    }

    public AuthResponseDTO login(UsuarioLoginDTO dto) {
        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail());

        if (usuario == null) {
            throw new ResourceNotFoundException("Usuario no encontrado");
        }

        if (!usuario.getPassword().equals(dto.getPassword())) {
            throw new ArgumentInvalidException("Contrasena incorrecta");
        }

        return new AuthResponseDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getRol() != null ? usuario.getRol().name() : null);
    }
}

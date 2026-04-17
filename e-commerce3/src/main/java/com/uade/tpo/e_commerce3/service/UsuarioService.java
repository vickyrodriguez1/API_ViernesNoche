package com.uade.tpo.e_commerce3.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.e_commerce3.dto.UsuarioRegistroDTO;
import com.uade.tpo.e_commerce3.dto.UsuarioUpdateDTO;
import com.uade.tpo.e_commerce3.exception.ResourceNotFoundException;
import com.uade.tpo.e_commerce3.model.RolEnum;
import com.uade.tpo.e_commerce3.model.Usuario;
import com.uade.tpo.e_commerce3.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario getUsuarioById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
    }

    public Usuario getUsuarioByEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario == null) {
            throw new ResourceNotFoundException("Usuario no encontrado con email: " + email);
        }
        return usuario;
    }

    public void deleteUsuarioById(Long id) {
        usuarioRepository.deleteById(id);
    }

    /*public Usuario saveUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }*/

    public Usuario saveUsuario(UsuarioRegistroDTO dto) {
    Usuario usuario = new Usuario();
    usuario.setNombre(dto.getNombre());
    usuario.setApellido(dto.getApellido());
    usuario.setEmail(dto.getEmail());
    usuario.setPassword(dto.getPassword());

    // Conversión de String a Enum
    if (dto.getRol() != null) {
        usuario.setRol(RolEnum.valueOf(dto.getRol().toUpperCase()));
    }

    return usuarioRepository.save(usuario);
}

    public Usuario updateUsuario(Long id, UsuarioUpdateDTO dto) {
        Usuario existingUsuario = getUsuarioById(id);
        existingUsuario.setNombre(dto.getNombre());
        existingUsuario.setApellido(dto.getApellido());
        existingUsuario.setTelefono(dto.getTelefono());
        existingUsuario.setDireccion(dto.getDireccion());
        return usuarioRepository.save(existingUsuario);
    }
}

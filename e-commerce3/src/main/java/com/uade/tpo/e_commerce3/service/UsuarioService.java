package com.uade.tpo.e_commerce3.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.e_commerce3.exception.ResourceNotFoundException;
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

    public Usuario saveUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Usuario updateUsuario(Long id, Usuario usuario) {
        Usuario existingUsuario = getUsuarioById(id);
        existingUsuario.setNombre(usuario.getNombre());
        existingUsuario.setApellido(usuario.getApellido());
        existingUsuario.setEmail(usuario.getEmail());
        existingUsuario.setTelefono(usuario.getTelefono());
        existingUsuario.setDireccion(usuario.getDireccion());
        return usuarioRepository.save(existingUsuario);
    }
}

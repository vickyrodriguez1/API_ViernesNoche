package com.uade.tpo.e_commerce3.service;

import java.util.List;
import java.util.Optional;

import com.uade.tpo.e_commerce3.config.SecurityUser;
import com.uade.tpo.e_commerce3.exception.ArgumentInvalidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.uade.tpo.e_commerce3.dto.UsuarioRegistroDTO;
import com.uade.tpo.e_commerce3.dto.UsuarioUpdateDTO;
import com.uade.tpo.e_commerce3.exception.ResourceNotFoundException;
import com.uade.tpo.e_commerce3.model.Usuario;
import com.uade.tpo.e_commerce3.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UsuarioService implements UserDetailsService {
    
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
        return usuarioRepository.findByEmail(email)
                .orElseThrow(()-> new ResourceNotFoundException("Usuario no encontrado con email: " + email));
    }

    public void deleteUsuarioById(Long id) {
        usuarioRepository.deleteById(id);
    }

    public Usuario updateUsuario(Long id, UsuarioUpdateDTO dto) {
        Usuario existingUsuario = getUsuarioById(id);
        existingUsuario.setNombre(dto.getNombre());
        existingUsuario.setApellido(dto.getApellido());
        existingUsuario.setTelefono(dto.getTelefono());
        existingUsuario.setDireccion(dto.getDireccion());
        return usuarioRepository.save(existingUsuario);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Usuario user = this.getUsuarioByEmail(username);
        return new SecurityUser(user);
    }
}

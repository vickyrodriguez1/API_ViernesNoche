package com.uade.tpo.e_commerce3.service;

import java.util.List;
import java.util.stream.Collectors;

import com.uade.tpo.e_commerce3.config.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.uade.tpo.e_commerce3.dto.UsuarioResponseDTO;
import com.uade.tpo.e_commerce3.dto.UsuarioUpdateDTO;
import com.uade.tpo.e_commerce3.exception.ResourceNotFoundException;
import com.uade.tpo.e_commerce3.model.Usuario;
import com.uade.tpo.e_commerce3.repository.UsuarioRepository;
import com.uade.tpo.e_commerce3.service.mapper.UsuarioMapper;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UsuarioService implements UserDetailsService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioMapper usuarioMapper;

    public List<UsuarioResponseDTO> getAllUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(usuarioMapper::toDto)
                .collect(Collectors.toList());
    }

    public UsuarioResponseDTO getUsuarioById(Long id) {
        return usuarioMapper.toDto(getUsuarioEntityById(id));
    }

    public UsuarioResponseDTO getUsuarioByEmail(String email) {
        return usuarioMapper.toDto(getUsuarioEntityByEmail(email));
    }

    public void deleteUsuarioById(Long id) {
        usuarioRepository.deleteById(id);
    }

    public UsuarioResponseDTO updateUsuario(Long id, UsuarioUpdateDTO dto) {
        Usuario existingUsuario = getUsuarioEntityById(id);
        existingUsuario.setNombre(dto.getNombre());
        existingUsuario.setApellido(dto.getApellido());
        existingUsuario.setTelefono(dto.getTelefono());
        existingUsuario.setDireccion(dto.getDireccion());
        return usuarioMapper.toDto(usuarioRepository.save(existingUsuario));
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Usuario user = this.getUsuarioEntityByEmail(username);
        return new SecurityUser(user);
    }

    public Usuario getUsuarioEntityById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
    }

    public Usuario getUsuarioEntityByEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + email));
    }
}

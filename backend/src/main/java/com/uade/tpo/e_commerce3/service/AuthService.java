package com.uade.tpo.e_commerce3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.uade.tpo.e_commerce3.config.JwtService;
import com.uade.tpo.e_commerce3.dto.AuthResponseDTO;
import com.uade.tpo.e_commerce3.dto.TokenResponseDTO;
import com.uade.tpo.e_commerce3.dto.UsuarioLoginDTO;
import com.uade.tpo.e_commerce3.dto.UsuarioRegistroDTO;
import com.uade.tpo.e_commerce3.exception.ArgumentInvalidException;
import com.uade.tpo.e_commerce3.model.Usuario;
import com.uade.tpo.e_commerce3.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    public AuthResponseDTO registrar(UsuarioRegistroDTO dto) {
        if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new ArgumentInvalidException("Ya existe un usuario con ese email");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setEmail(dto.getEmail());
        String hashedPassword = passwordEncoder.encode(dto.getPassword());
        usuario.setPassword(hashedPassword);
        usuario.setTelefono(dto.getTelefono());
        usuario.setDireccion(dto.getDireccion());
        usuario.setRol(dto.getRol());
        usuarioRepository.save(usuario);
        return new AuthResponseDTO(usuario.getId(), usuario.getNombre(), usuario.getApellido(), usuario.getEmail(), usuario.getRol().name());
    }

    public TokenResponseDTO login(UsuarioLoginDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);
        return new TokenResponseDTO(token);
    }
}

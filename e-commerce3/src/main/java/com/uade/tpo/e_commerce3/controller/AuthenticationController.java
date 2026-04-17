package com.uade.tpo.e_commerce3.controller;

import com.uade.tpo.e_commerce3.config.JwtService;
import com.uade.tpo.e_commerce3.dto.TokenResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.e_commerce3.dto.AuthResponseDTO;
import com.uade.tpo.e_commerce3.dto.UsuarioLoginDTO;
import com.uade.tpo.e_commerce3.dto.UsuarioRegistroDTO;
import com.uade.tpo.e_commerce3.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private AuthService authService;

    @Autowired
    private  AuthenticationManager authenticationManager;

    @Autowired
    private  JwtService jwtService;


    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> registrar(@RequestBody UsuarioRegistroDTO dto) {
            AuthResponseDTO response = authService.registrar(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody UsuarioLoginDTO request) {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtService.generateToken(userDetails);
            return ResponseEntity.ok(new TokenResponseDTO(token));
    }
}

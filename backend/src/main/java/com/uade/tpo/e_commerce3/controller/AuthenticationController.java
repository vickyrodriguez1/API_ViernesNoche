package com.uade.tpo.e_commerce3.controller;

import com.uade.tpo.e_commerce3.dto.TokenResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.e_commerce3.dto.AuthResponseDTO;
import com.uade.tpo.e_commerce3.dto.UsuarioLoginDTO;
import com.uade.tpo.e_commerce3.dto.UsuarioRegistroDTO;
import com.uade.tpo.e_commerce3.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> registrar(@Valid @RequestBody UsuarioRegistroDTO dto) {
            return ResponseEntity.status(HttpStatus.CREATED).body(authService.registrar(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@Valid @RequestBody UsuarioLoginDTO request) {
            return ResponseEntity.ok(authService.login(request));
    }
}

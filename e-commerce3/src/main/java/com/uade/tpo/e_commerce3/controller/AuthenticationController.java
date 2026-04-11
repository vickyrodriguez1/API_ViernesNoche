package com.uade.tpo.e_commerce3.controller;

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

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> registrar(@RequestBody UsuarioRegistroDTO dto) {
        try {
            AuthResponseDTO response = authService.registrar(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UsuarioLoginDTO dto) {
        try {
            AuthResponseDTO response = authService.login(dto);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}

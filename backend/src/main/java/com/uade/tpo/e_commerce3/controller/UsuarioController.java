package com.uade.tpo.e_commerce3.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.e_commerce3.dto.UsuarioResponseDTO;
import com.uade.tpo.e_commerce3.dto.UsuarioUpdateDTO;
import com.uade.tpo.e_commerce3.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // GET all usuarios
    // http://localhost:8080/api/usuarios
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> getAllUsuarios() {
        return ResponseEntity.ok(usuarioService.getAllUsuarios());
    }

    // GET usuario by id
    // http://localhost:8080/api/usuarios/1
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> getUsuarioById(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.getUsuarioById(id));
    }

    // GET usuario by email
    // http://localhost:8080/api/usuarios/email/example@email.com
    @GetMapping("/email/{email}")
    public ResponseEntity<UsuarioResponseDTO> getUsuarioByEmail(@PathVariable String email) {
        return ResponseEntity.ok(usuarioService.getUsuarioByEmail(email));
    }

    // PUT - actualizar usuario
    // http://localhost:8080/api/usuarios/1
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> updateUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioUpdateDTO request) {
        return ResponseEntity.ok(usuarioService.updateUsuario(id, request));
    }

    // DELETE - eliminar usuario
    // http://localhost:8080/api/usuarios/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuarioById(@PathVariable Long id) {
        usuarioService.deleteUsuarioById(id);
        return ResponseEntity.noContent().build();
    }
}

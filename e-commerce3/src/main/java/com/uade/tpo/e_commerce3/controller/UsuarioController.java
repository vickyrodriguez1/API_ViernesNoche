package com.uade.tpo.e_commerce3.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.e_commerce3.model.Usuario;
import com.uade.tpo.e_commerce3.service.UsuarioService;
//import com.uade.tpo.e_commerce3.dto.UsuarioRegistroDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // GET all usuarios
    // http://localhost:8080/api/usuarios
    @GetMapping
    public List<Usuario> getAllUsuarios() {
        return usuarioService.getAllUsuarios();
    }

    // GET usuario by id
    // http://localhost:8080/api/usuarios/1
    @GetMapping("/{id}")
    public Usuario getUsuarioById(@PathVariable Long id) {
        return usuarioService.getUsuarioById(id);
    }

    // GET usuario by email
    // http://localhost:8080/api/usuarios/email/example@email.com
    @GetMapping("/email/{email}")
    public Usuario getUsuarioByEmail(@PathVariable String email) {
        return usuarioService.getUsuarioByEmail(email);
    }

    // POST - crear nuevo usuario
   // http://localhost:8080/api/usuarios
    @PostMapping
   public Usuario saveUsuario(@Valid @RequestBody Usuario usuario) {
       return usuarioService.saveUsuario(usuario);
   }

 

    // PUT - actualizar usuario
    // http://localhost:8080/api/usuarios/1
    @PutMapping("/{id}")
    public Usuario updateUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        return usuarioService.updateUsuario(id, usuario);
    }

    // DELETE - eliminar usuario
    // http://localhost:8080/api/usuarios/1
    @DeleteMapping("/{id}")
    public void deleteUsuarioById(@PathVariable Long id) {
        usuarioService.deleteUsuarioById(id);
    }
}

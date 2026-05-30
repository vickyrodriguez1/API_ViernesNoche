package com.uade.tpo.e_commerce3.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.e_commerce3.dto.CategoriaRequestDTO;
import com.uade.tpo.e_commerce3.dto.CategoriaResponseDTO;
import com.uade.tpo.e_commerce3.service.CategoriaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<CategoriaResponseDTO>> getAllCategorias() {
        return ResponseEntity.ok(categoriaService.getAllCategorias());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> getCategoriaById(@PathVariable Long id) {
        return ResponseEntity.ok(categoriaService.getCategoriaById(id));
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<CategoriaResponseDTO> getCategoriaByNombre(@PathVariable String nombre) {
        return ResponseEntity.ok(categoriaService.getCategoriaByNombre(nombre));
    }

    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> saveCategoria(@Valid @RequestBody CategoriaRequestDTO categoriaDto) {
        CategoriaResponseDTO created = categoriaService.saveCategoria(categoriaDto);
        return ResponseEntity.created(URI.create("/api/categorias/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> updateCategoria(@PathVariable Long id, @Valid @RequestBody CategoriaRequestDTO categoriaDto) {
        return ResponseEntity.ok(categoriaService.updateCategoria(id, categoriaDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoria(@PathVariable Long id) {
        categoriaService.deleteCategoriaById(id);
        return ResponseEntity.noContent().build();
    }
}

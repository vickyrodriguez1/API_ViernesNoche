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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.e_commerce3.dto.ProductoListDTO;
import com.uade.tpo.e_commerce3.dto.ProductoRequestDTO;
import com.uade.tpo.e_commerce3.dto.ProductoResponseDTO;
import com.uade.tpo.e_commerce3.service.ProductoService;

import jakarta.validation.Valid;

@RestController
// para acceder a este controlador, la URL base será /api/productos
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    // GET all productos con filtros opcionales
    // http://localhost:8080/api/productos
    // http://localhost:8080/api/productos?nombre=remera
    // http://localhost:8080/api/productos?orden=asc
    // http://localhost:8080/api/productos?nombre=remera&orden=desc
    @GetMapping
    public ResponseEntity<List<ProductoListDTO>> getAllProductos(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String orden) {

        return ResponseEntity.ok(productoService.getProductosFiltrados(nombre, orden));
    }

    // GET producto by id (detalle completo)
    // http://localhost:8080/api/productos/1
    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> getProductoById(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.getProductoById(id));
    }


    // RUTAS PARA ADMINS:

    // DELETE - eliminar producto
    // http://localhost:8080/api/productos/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductoById(@PathVariable Long id) {
        productoService.deleteProductoById(id);
        return ResponseEntity.noContent().build();
    }

    // POST - crear nuevo producto
    // http://localhost:8080/api/productos
    @PostMapping
    public ResponseEntity<ProductoResponseDTO> saveProducto(@Valid @RequestBody ProductoRequestDTO request) {
        ProductoResponseDTO created = productoService.saveProducto(request);
        return ResponseEntity.created(URI.create("/api/productos/" + created.getId())).body(created);
    }
    
    // PUT - actualizar producto
    // http://localhost:8080/api/productos/1
    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> updateProducto(@PathVariable Long id, @Valid @RequestBody ProductoRequestDTO request) {
        return ResponseEntity.ok(productoService.updateProducto(id, request));
    }
}

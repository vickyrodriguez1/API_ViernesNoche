package com.uade.tpo.e_commerce3.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.e_commerce3.exception.ResourceNotFoundException;
import com.uade.tpo.e_commerce3.model.Categoria;
import com.uade.tpo.e_commerce3.repository.CategoriaRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CategoriaService {
    
    @Autowired
    private CategoriaRepository categoriaRepository;
    
    public List<Categoria> getAllCategorias() {
        return categoriaRepository.findAll();
    }

    public Categoria getCategoriaById(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria no encontrada con id: " + id));
    }

    public Categoria getCategoriaByNombre(String nombre) {
        Categoria categoria = categoriaRepository.findByNombre(nombre);
        if (categoria == null) {
            throw new ResourceNotFoundException("Categoria no encontrada con nombre: " + nombre);
        }
        return categoria;
    }

    public void deleteCategoriaById(Long id) {
        categoriaRepository.deleteById(id);
    }

    public Categoria saveCategoria(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    public Categoria updateCategoria(Long id, Categoria categoria) {
        Categoria existingCategoria = getCategoriaById(id);
        existingCategoria.setNombre(categoria.getNombre());
        return categoriaRepository.save(existingCategoria);
    }
}

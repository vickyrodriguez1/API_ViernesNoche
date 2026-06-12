package com.uade.tpo.e_commerce3.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.e_commerce3.dto.CategoriaRequestDTO;
import com.uade.tpo.e_commerce3.dto.CategoriaResponseDTO;
import com.uade.tpo.e_commerce3.exception.ResourceNotFoundException;
import com.uade.tpo.e_commerce3.model.Categoria;
import com.uade.tpo.e_commerce3.repository.CategoriaRepository;
import com.uade.tpo.e_commerce3.service.mapper.CategoriaMapper;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CategoriaService {
    
    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private CategoriaMapper categoriaMapper;

    public List<CategoriaResponseDTO> getAllCategorias() {
        return categoriaRepository.findAll().stream()
                .map(categoriaMapper::toDto)
                .collect(Collectors.toList());
    }

    public CategoriaResponseDTO getCategoriaById(Long id) {
        return categoriaMapper.toDto(getCategoriaEntityById(id));
    }

    public CategoriaResponseDTO getCategoriaByNombre(String nombre) {
        Categoria categoria = categoriaRepository.findByNombre(nombre);
        if (categoria == null) {
            throw new ResourceNotFoundException("Categoria no encontrada con nombre: " + nombre);
        }
        return categoriaMapper.toDto(categoria);
    }

    public void deleteCategoriaById(Long id) {
        categoriaRepository.deleteById(id);
    }

    public CategoriaResponseDTO saveCategoria(CategoriaRequestDTO categoriaDto) {
        Categoria categoria = new Categoria();
        categoria.setNombre(categoriaDto.getNombre());
        return categoriaMapper.toDto(categoriaRepository.save(categoria));
    }

    public CategoriaResponseDTO updateCategoria(Long id, CategoriaRequestDTO categoriaDto) {
        Categoria existingCategoria = getCategoriaEntityById(id);
        existingCategoria.setNombre(categoriaDto.getNombre());
        return categoriaMapper.toDto(categoriaRepository.save(existingCategoria));
    }

    private Categoria getCategoriaEntityById(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria no encontrada con id: " + id));
    }
}

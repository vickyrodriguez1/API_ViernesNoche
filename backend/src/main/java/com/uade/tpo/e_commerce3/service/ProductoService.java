package com.uade.tpo.e_commerce3.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.e_commerce3.dto.ProductoListDTO;
import com.uade.tpo.e_commerce3.dto.ProductoRequestDTO;
import com.uade.tpo.e_commerce3.dto.ProductoResponseDTO;
import com.uade.tpo.e_commerce3.exception.ResourceNotFoundException;
import com.uade.tpo.e_commerce3.model.Categoria;
import com.uade.tpo.e_commerce3.model.Producto;
import com.uade.tpo.e_commerce3.repository.CategoriaRepository;
import com.uade.tpo.e_commerce3.repository.ProductoRepository;
import com.uade.tpo.e_commerce3.service.mapper.ProductoMapper;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProductoService {
 
    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProductoMapper productoMapper;

    public List<ProductoListDTO> getProductosFiltrados(String nombre, String orden) {
        List<Producto> productos = productoRepository.findAll();
        if (nombre != null && !nombre.isBlank()) {
            productos = productos.stream()
                    .filter(p -> p.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                    .collect(Collectors.toList());
        }
        if ("desc".equalsIgnoreCase(orden)) {
            productos = productos.stream()
                    .sorted((a, b) -> Double.compare(b.getPrecio(), a.getPrecio()))
                    .collect(Collectors.toList());
        } else if ("asc".equalsIgnoreCase(orden)) {
            productos = productos.stream()
                    .sorted((a, b) -> Double.compare(a.getPrecio(), b.getPrecio()))
                    .collect(Collectors.toList());
        }

        return productos.stream()
                .map(productoMapper::toListDto)
                .collect(Collectors.toList());
    }

    public ProductoResponseDTO getProductoById(Long id) {
        return productoMapper.toDto(getProductoEntityById(id));
    }

    private Producto getProductoEntityById(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
    }

    public void deleteProductoById(Long id) {
        productoRepository.deleteById(id);
    }

    public ProductoResponseDTO saveProducto(ProductoRequestDTO request) {
        Producto producto = new Producto();
        producto.setNombre(request.getNombre());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecio(request.getPrecio());
        producto.setStock(request.getStock());

        if (request.getCategoriaIds() != null && !request.getCategoriaIds().isEmpty()) {
            List<Categoria> categorias = categoriaRepository.findAllById(request.getCategoriaIds());
            producto.setCategorias(categorias);
        }

        return productoMapper.toDto(productoRepository.save(producto));
    }

    public ProductoResponseDTO updateProducto(Long id, ProductoRequestDTO request) {
        Producto existingProducto = getProductoEntityById(id);
        existingProducto.setNombre(request.getNombre());
        existingProducto.setDescripcion(request.getDescripcion());
        existingProducto.setPrecio(request.getPrecio());
        existingProducto.setStock(request.getStock());

        existingProducto.getCategorias().clear();
        if (request.getCategoriaIds() != null && !request.getCategoriaIds().isEmpty()) {
            List<Categoria> categorias = categoriaRepository.findAllById(request.getCategoriaIds());
            existingProducto.getCategorias().addAll(categorias);
        }

        return productoMapper.toDto(productoRepository.save(existingProducto));
    }
}

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

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProductoService {
 
    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;
    
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
                .map(ProductoListDTO::toDto)
                .collect(Collectors.toList());
    }

    public ProductoResponseDTO getProductoById(Long id) {
        return ProductoResponseDTO.toDto(findProductoOrThrow(id));
    }

    private Producto findProductoOrThrow(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
    }

    public void deleteProductoById(Long id) {
        productoRepository.deleteById(id);
    }

    public Producto saveProducto(ProductoRequestDTO request) {
        Producto producto = new Producto();
        producto.setNombre(request.getNombre());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecio(request.getPrecio());
        producto.setStock(request.getStock());

        if (request.getCategoriaIds() != null && !request.getCategoriaIds().isEmpty()) {
            List<Categoria> categorias = categoriaRepository.findAllById(request.getCategoriaIds());
            producto.setCategorias(categorias);
        }

        return productoRepository.save(producto);
    }

    public Producto updateProducto(Long id, Producto producto) {
        Producto existingProducto = findProductoOrThrow(id);
        existingProducto.setNombre(producto.getNombre());
        existingProducto.setDescripcion(producto.getDescripcion());
        existingProducto.setPrecio(producto.getPrecio());
        existingProducto.setStock(producto.getStock());
        existingProducto.getCategorias().clear();
        if (producto.getCategorias() != null) {
            existingProducto.getCategorias().addAll(producto.getCategorias());
        }
        return productoRepository.save(existingProducto);
    }
}

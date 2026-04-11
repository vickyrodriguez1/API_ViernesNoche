package com.uade.tpo.e_commerce3.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.e_commerce3.dto.ProductoRequestDTO;
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
    
    public List<Producto> getAllProductos() {
        return productoRepository.findAll();
    }

    public Producto getProductoById(Long id) {
        return productoRepository.findById(id).orElse(null);
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
        Producto existingProducto = getProductoById(id);
        if (existingProducto != null) {
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
        return null;
    }
}

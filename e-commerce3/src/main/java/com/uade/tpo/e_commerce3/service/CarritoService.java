package com.uade.tpo.e_commerce3.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.e_commerce3.dto.CarritoResponseDTO;
import com.uade.tpo.e_commerce3.exception.ArgumentInvalidException;
import com.uade.tpo.e_commerce3.exception.ResourceNotFoundException;
import com.uade.tpo.e_commerce3.model.Carrito;
import com.uade.tpo.e_commerce3.model.ItemCarrito;
import com.uade.tpo.e_commerce3.model.Producto;
import com.uade.tpo.e_commerce3.model.Usuario;
import com.uade.tpo.e_commerce3.repository.CarritoRepository;
import com.uade.tpo.e_commerce3.repository.ItemCarritoRepository;
import com.uade.tpo.e_commerce3.repository.ProductoRepository;
import com.uade.tpo.e_commerce3.repository.UsuarioRepository;
import com.uade.tpo.e_commerce3.service.mapper.CarritoMapper;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private ItemCarritoRepository itemCarritoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CarritoMapper carritoMapper;

    public CarritoResponseDTO crearCarrito(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + usuarioId));

        if (usuario.getCarrito() != null) {
            throw new ArgumentInvalidException("El usuario ya tiene un carrito asociado");
        }

        Carrito carrito = new Carrito();
        carrito.setUsuario(usuario);
        carrito.setFechaCreacion(LocalDateTime.now());
        carrito.setFechaActualizacion(LocalDateTime.now());

        return carritoMapper.toDto(carritoRepository.save(carrito));
    }

    public CarritoResponseDTO getCarritoById(Long id) {
        Carrito carrito = carritoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Carrito no encontrado con id: " + id));
        return carritoMapper.toDto(carrito);
    }

    public CarritoResponseDTO getCarritoPorUsuario(Long usuarioId) {
        Carrito carrito = carritoRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("El usuario no tiene un carrito asociado"));
        return carritoMapper.toDto(carrito);
    }

    public CarritoResponseDTO agregarProducto(Long carritoId, Long productoId, Integer cantidad) {
        Carrito carrito = getCarritoEntityById(carritoId);

        if (cantidad == null || cantidad <= 0) {
            throw new ArgumentInvalidException("La cantidad debe ser mayor a 0");
        }

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + productoId));

        Optional<ItemCarrito> itemExistente = itemCarritoRepository.findByCarritoIdAndProductoId(carritoId, productoId);

        if (itemExistente.isPresent()) {
            ItemCarrito item = itemExistente.get();
            item.setCantidad(item.getCantidad() + cantidad);
            itemCarritoRepository.save(item);
        } else {
            ItemCarrito nuevoItem = new ItemCarrito();
            nuevoItem.setCarrito(carrito);
            nuevoItem.setProducto(producto);
            nuevoItem.setCantidad(cantidad);
            nuevoItem.setPrecioUnitario(producto.getPrecio());
            itemCarritoRepository.save(nuevoItem);
            carrito.getItems().add(nuevoItem);
        }

        carrito.setFechaActualizacion(LocalDateTime.now());
        return carritoMapper.toDto(carritoRepository.save(carrito));
    }

    public CarritoResponseDTO eliminarProducto(Long carritoId, Long itemId) {
        Carrito carrito = getCarritoEntityById(carritoId);

        ItemCarrito item = itemCarritoRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item de carrito no encontrado con id: " + itemId));

        if (!item.getCarrito().getId().equals(carritoId)) {
            throw new ArgumentInvalidException("El item no pertenece al carrito indicado");
        }

        carrito.getItems().remove(item);
        itemCarritoRepository.deleteById(itemId);

        carrito.setFechaActualizacion(LocalDateTime.now());
        return carritoMapper.toDto(carritoRepository.save(carrito));
    }

    public CarritoResponseDTO actualizarCantidad(Long carritoId, Long itemId, Integer nuevaCantidad) {
        Carrito carrito = getCarritoEntityById(carritoId);

        if (nuevaCantidad == null || nuevaCantidad <= 0) {
            throw new ArgumentInvalidException("La nueva cantidad debe ser mayor a 0");
        }

        ItemCarrito item = itemCarritoRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item de carrito no encontrado con id: " + itemId));

        if (!item.getCarrito().getId().equals(carritoId)) {
            throw new ArgumentInvalidException("El item no pertenece al carrito indicado");
        }

        item.setCantidad(nuevaCantidad);
        itemCarritoRepository.save(item);

        carrito.setFechaActualizacion(LocalDateTime.now());
        return carritoMapper.toDto(carritoRepository.save(carrito));
    }

    public CarritoResponseDTO vaciarCarrito(Long carritoId) {
        Carrito carrito = getCarritoEntityById(carritoId);

        carrito.getItems().clear();
        carrito.setFechaActualizacion(LocalDateTime.now());
        return carritoMapper.toDto(carritoRepository.save(carrito));
    }

    public Double calcularTotal(Long carritoId) {
        Carrito carrito = getCarritoEntityById(carritoId);

        return carrito.getItems().stream()
                .mapToDouble(item -> item.getPrecioUnitario() * item.getCantidad())
                .sum();
    }

    public CarritoResponseDTO completarCarrito(Long carritoId) {
        Carrito carrito = getCarritoEntityById(carritoId);
        carrito.setFechaActualizacion(LocalDateTime.now());
        return carritoMapper.toDto(carritoRepository.save(carrito));
    }

    public void deleteCarrito(Long carritoId) {
        carritoRepository.deleteById(carritoId);
    }

    private Carrito getCarritoEntityById(Long id) {
        return carritoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Carrito no encontrado con id: " + id));
    }
}

package com.uade.tpo.e_commerce3.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    // Obtener o crear carrito del usuario
    public Carrito obtenerCarritoDelUsuario(Long usuarioId) {
        Optional<Carrito> carrito = carritoRepository.findByUsuarioId(usuarioId);
        if (carrito.isPresent()) {
            return carrito.get();
        }
        // Si no existe, crear uno nuevo
        return crearCarrito(usuarioId);
    }

    // Crear un nuevo carrito
    public Carrito crearCarrito(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + usuarioId));

        // Verificar si ya tiene un carrito
        if (usuario.getCarrito() != null) {
            throw new ArgumentInvalidException("El usuario ya tiene un carrito asociado");
        }

        Carrito carrito = new Carrito();
        carrito.setUsuario(usuario);
        carrito.setFechaCreacion(LocalDateTime.now());
        carrito.setFechaActualizacion(LocalDateTime.now());

        return carritoRepository.save(carrito);
    }

    // Obtener carrito por ID
    public Carrito getCarritoById(Long id) {
        return carritoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Carrito no encontrado con id: " + id));
    }

    // Obtener carrito del usuario
    public Carrito getCarritoPorUsuario(Long usuarioId) {
        return carritoRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("El usuario no tiene un carrito asociado"));
    }

    // Agregar producto al carrito
    public Carrito agregarProducto(Long carritoId, Long productoId, Integer cantidad) {
        Carrito carrito = getCarritoById(carritoId);

        if (cantidad == null || cantidad <= 0) {
            throw new ArgumentInvalidException("La cantidad debe ser mayor a 0");
        }

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + productoId));

        Optional<ItemCarrito> itemExistente = itemCarritoRepository.findByCarritoIdAndProductoId(carritoId, productoId);

        if (itemExistente.isPresent()) {
            // Si existe, aumentar la cantidad
            ItemCarrito item = itemExistente.get();
            item.setCantidad(item.getCantidad() + cantidad);
            itemCarritoRepository.save(item);
        } else {
            // Si no existe, crear nuevo item
            ItemCarrito nuevoItem = new ItemCarrito();
            nuevoItem.setCarrito(carrito);
            nuevoItem.setProducto(producto);
            nuevoItem.setCantidad(cantidad);
            nuevoItem.setPrecioUnitario(producto.getPrecio());
            itemCarritoRepository.save(nuevoItem);
            carrito.getItems().add(nuevoItem);
        }

        carrito.setFechaActualizacion(LocalDateTime.now());
        return carritoRepository.save(carrito);
    }

    // Eliminar producto del carrito
    public Carrito eliminarProducto(Long carritoId, Long itemId) {
        Carrito carrito = getCarritoById(carritoId);

        ItemCarrito item = itemCarritoRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item de carrito no encontrado con id: " + itemId));

        if (!item.getCarrito().getId().equals(carritoId)) {
            throw new ArgumentInvalidException("El item no pertenece al carrito indicado");
        }

        carrito.getItems().remove(item);
        itemCarritoRepository.deleteById(itemId);

        carrito.setFechaActualizacion(LocalDateTime.now());
        return carritoRepository.save(carrito);
    }

    // Actualizar cantidad de un producto en el carrito
    public Carrito actualizarCantidad(Long carritoId, Long itemId, Integer nuevaCantidad) {
        Carrito carrito = getCarritoById(carritoId);

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
        return carritoRepository.save(carrito);
    }

    // Vaciar carrito
    public Carrito vaciarCarrito(Long carritoId) {
        Carrito carrito = getCarritoById(carritoId);

        carrito.getItems().clear();
        carrito.setFechaActualizacion(LocalDateTime.now());
        return carritoRepository.save(carrito);
    }

    // Obtener total del carrito
    public Double calcularTotal(Long carritoId) {
        Carrito carrito = getCarritoById(carritoId);

        return carrito.getItems().stream()
                .mapToDouble(item -> item.getPrecioUnitario() * item.getCantidad())
                .sum();
    }

    // Completar carrito (cambiar estado a completado y crear nuevo carrito)
    public Carrito completarCarrito(Long carritoId) {
        Carrito carrito = getCarritoById(carritoId);
        
        carrito.setFechaActualizacion(LocalDateTime.now());
        return carritoRepository.save(carrito);
    }

    // Eliminar carrito
    public void deleteCarrito(Long carritoId) {
        carritoRepository.deleteById(carritoId);
    }
}

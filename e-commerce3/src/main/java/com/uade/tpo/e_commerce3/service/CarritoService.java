package com.uade.tpo.e_commerce3.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    // Obtener o crear carrito activo del usuario
    public Carrito obtenerCarritoActivo(Long usuarioId) {
        Optional<Carrito> carrito = carritoRepository.findByUsuarioIdAndEstado(usuarioId, "activo");
        if (carrito.isPresent()) {
            return carrito.get();
        }
        // Si no existe, crear uno nuevo
        return crearCarrito(usuarioId);
    }

    // Crear un nuevo carrito
    public Carrito crearCarrito(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
        if (usuario == null) {
            return null;
        }

        Carrito carrito = new Carrito();
        carrito.setUsuario(usuario);
        carrito.setFechaCreacion(LocalDateTime.now());
        carrito.setFechaActualizacion(LocalDateTime.now());
        carrito.setEstado("activo");

        return carritoRepository.save(carrito);
    }

    // Obtener carrito por ID
    public Carrito getCarritoById(Long id) {
        return carritoRepository.findById(id).orElse(null);
    }

    // Obtener todos los carritos del usuario
    public List<Carrito> getCarritosPorUsuario(Long usuarioId) {
        return carritoRepository.findByUsuarioId(usuarioId);
    }

    // Agregar producto al carrito
    public Carrito agregarProducto(Long carritoId, Long productoId, Integer cantidad) {
        Carrito carrito = carritoRepository.findById(carritoId).orElse(null);
        if (carrito == null) {
            return null;
        }

        Producto producto = productoRepository.findById(productoId).orElse(null);
        if (producto == null || cantidad <= 0) {
            return null;
        }

        // Verificar si el producto ya está en el carrito
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
        Carrito carrito = carritoRepository.findById(carritoId).orElse(null);
        if (carrito == null) {
            return null;
        }

        ItemCarrito item = itemCarritoRepository.findById(itemId).orElse(null);
        if (item != null && item.getCarrito().getId().equals(carritoId)) {
            carrito.getItems().remove(item);
            itemCarritoRepository.deleteById(itemId);
        }

        carrito.setFechaActualizacion(LocalDateTime.now());
        return carritoRepository.save(carrito);
    }

    // Actualizar cantidad de un producto en el carrito
    public Carrito actualizarCantidad(Long carritoId, Long itemId, Integer nuevaCantidad) {
        Carrito carrito = carritoRepository.findById(carritoId).orElse(null);
        if (carrito == null || nuevaCantidad <= 0) {
            return null;
        }

        ItemCarrito item = itemCarritoRepository.findById(itemId).orElse(null);
        if (item != null && item.getCarrito().getId().equals(carritoId)) {
            item.setCantidad(nuevaCantidad);
            itemCarritoRepository.save(item);
        }

        carrito.setFechaActualizacion(LocalDateTime.now());
        return carritoRepository.save(carrito);
    }

    // Vaciar carrito
    public Carrito vaciarCarrito(Long carritoId) {
        Carrito carrito = carritoRepository.findById(carritoId).orElse(null);
        if (carrito == null) {
            return null;
        }

        carrito.getItems().clear();
        carrito.setFechaActualizacion(LocalDateTime.now());
        return carritoRepository.save(carrito);
    }

    // Obtener total del carrito
    public Double calcularTotal(Long carritoId) {
        Carrito carrito = carritoRepository.findById(carritoId).orElse(null);
        if (carrito == null) {
            return 0.0;
        }

        return carrito.getItems().stream()
                .mapToDouble(item -> item.getPrecioUnitario() * item.getCantidad())
                .sum();
    }

    // Completar carrito (cambiar estado)
    public Carrito completarCarrito(Long carritoId) {
        Carrito carrito = carritoRepository.findById(carritoId).orElse(null);
        if (carrito == null) {
            return null;
        }

        carrito.setEstado("completado");
        carrito.setFechaActualizacion(LocalDateTime.now());
        return carritoRepository.save(carrito);
    }

    // Eliminar carrito
    public void deleteCarrito(Long carritoId) {
        carritoRepository.deleteById(carritoId);
    }
}

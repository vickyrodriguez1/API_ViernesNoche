package com.uade.tpo.e_commerce3.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.e_commerce3.exception.ArgumentInvalidException;
import com.uade.tpo.e_commerce3.exception.ResourceNotFoundException;
import com.uade.tpo.e_commerce3.model.Carrito;
import com.uade.tpo.e_commerce3.model.Pedido;
import com.uade.tpo.e_commerce3.model.Producto;
import com.uade.tpo.e_commerce3.model.Usuario;
import com.uade.tpo.e_commerce3.repository.CarritoRepository;
import com.uade.tpo.e_commerce3.repository.PedidoRepository;
import com.uade.tpo.e_commerce3.repository.ProductoRepository;
import com.uade.tpo.e_commerce3.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    public Carrito obtenerCarritoDelUsuario(Long usuarioId) {
        Optional<Carrito> carrito = carritoRepository.findByUsuarioId(usuarioId);
        if (carrito.isPresent()) {
            return carrito.get();
        }
        return crearCarrito(usuarioId);
    }

    public Carrito crearCarrito(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + usuarioId));

        if (usuario.getCarrito() != null) {
            throw new ArgumentInvalidException("El usuario ya tiene un carrito asociado");
        }

        Carrito carrito = new Carrito();
        carrito.setUsuario(usuario);
        carrito.setFechaCreacion(LocalDateTime.now());
        carrito.setFechaActualizacion(LocalDateTime.now());

        return carritoRepository.save(carrito);
    }

    public Carrito getCarritoById(Long id) {
        return carritoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Carrito no encontrado con id: " + id));
    }

    public Carrito getCarritoPorUsuario(Long usuarioId) {
        return carritoRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("El usuario no tiene un carrito asociado"));
    }

    public Carrito agregarProducto(Long carritoId, Long productoId) {
        Carrito carrito = getCarritoById(carritoId);

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + productoId));

        boolean yaExiste = carrito.getProductos().stream()
                .anyMatch(p -> p.getId().equals(productoId));

        if (yaExiste) {
            throw new ArgumentInvalidException("El producto ya está en el carrito");
        }

        carrito.getProductos().add(producto);
        carrito.setFechaActualizacion(LocalDateTime.now());
        return carritoRepository.save(carrito);
    }

    public Carrito eliminarProducto(Long carritoId, Long productoId) {
        Carrito carrito = getCarritoById(carritoId);

        boolean removed = carrito.getProductos().removeIf(p -> p.getId().equals(productoId));

        if (!removed) {
            throw new ResourceNotFoundException("El producto con id " + productoId + " no está en el carrito");
        }

        carrito.setFechaActualizacion(LocalDateTime.now());
        return carritoRepository.save(carrito);
    }

    public Carrito vaciarCarrito(Long carritoId) {
        Carrito carrito = getCarritoById(carritoId);
        carrito.getProductos().clear();
        carrito.setFechaActualizacion(LocalDateTime.now());
        return carritoRepository.save(carrito);
    }

    public Double calcularTotal(Long carritoId) {
        Carrito carrito = getCarritoById(carritoId);
        return carrito.getProductos().stream()
                .mapToDouble(Producto::getPrecio)
                .sum();
    }

    public Pedido pagar(Long carritoId) {
        Carrito carrito = getCarritoById(carritoId);

        if (carrito.getProductos().isEmpty()) {
            throw new ArgumentInvalidException("El carrito está vacío, no se puede generar un pedido");
        }

        Pedido pedido = new Pedido();
        pedido.setUsuario(carrito.getUsuario());
        pedido.setProductos(new ArrayList<>(carrito.getProductos()));
        pedido.setPrecioTotal(calcularTotal(carritoId));
        pedido.setFechaPedido(LocalDateTime.now());
        pedido.setEstado("pendiente");

        pedidoRepository.save(pedido);

        carrito.getProductos().clear();
        carrito.setFechaActualizacion(LocalDateTime.now());
        carritoRepository.save(carrito);

        return pedido;
    }

    public void deleteCarrito(Long carritoId) {
        carritoRepository.deleteById(carritoId);
    }
}

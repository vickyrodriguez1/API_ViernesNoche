package com.uade.tpo.e_commerce3.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.e_commerce3.dto.CarritoResponseDTO;
import com.uade.tpo.e_commerce3.dto.PedidoResponseDTO;
import com.uade.tpo.e_commerce3.exception.ArgumentInvalidException;
import com.uade.tpo.e_commerce3.exception.ResourceNotFoundException;
import com.uade.tpo.e_commerce3.model.Carrito;
import com.uade.tpo.e_commerce3.model.Item;
import com.uade.tpo.e_commerce3.model.Pedido;
import com.uade.tpo.e_commerce3.model.Producto;
import com.uade.tpo.e_commerce3.model.Usuario;
import com.uade.tpo.e_commerce3.repository.CarritoRepository;
import com.uade.tpo.e_commerce3.repository.PedidoRepository;
import com.uade.tpo.e_commerce3.repository.ProductoRepository;
import com.uade.tpo.e_commerce3.repository.UsuarioRepository;
import com.uade.tpo.e_commerce3.service.mapper.CarritoMapper;
import com.uade.tpo.e_commerce3.service.mapper.PedidoMapper;

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

    @Autowired
    private CarritoMapper carritoMapper;

    @Autowired
    private PedidoMapper pedidoMapper;

    public CarritoResponseDTO obtenerCarritoDelUsuario(Long usuarioId) {
        Optional<Carrito> carrito = carritoRepository.findByUsuarioId(usuarioId);
        if (carrito.isPresent()) {
            return carritoMapper.toDto(carrito.get());
        }
        return crearCarrito(usuarioId);
    }

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
        return carritoMapper.toDto(getCarritoEntityById(id));
    }

    public CarritoResponseDTO getCarritoPorUsuario(Long usuarioId) {
        Carrito carrito = carritoRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("El usuario no tiene un carrito asociado"));
        return carritoMapper.toDto(carrito);
    }

    public CarritoResponseDTO agregarProducto(Long carritoId, Long productoId) {
        return agregarItem(carritoId, productoId, 1);
    }

    public CarritoResponseDTO agregarItem(Long carritoId, Long productoId, Integer cantidad) {
        if (cantidad == null || cantidad < 1) {
            throw new ArgumentInvalidException("La cantidad debe ser al menos 1");
        }

        Carrito carrito = getCarritoEntityById(carritoId);

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + productoId));

        Item item = carrito.getItems().stream()
                .filter(i -> i.getProducto().getId().equals(productoId))
                .findFirst()
                .orElse(null);

        if (item == null) {
            item = new Item();
            item.setProducto(producto);
            item.setCantidad(cantidad);
            item.setPrecioUnitario(producto.getPrecio());
            item.setCarrito(carrito);
            carrito.getItems().add(item);
        } else {
            item.setCantidad(item.getCantidad() + cantidad);
        }

        carrito.setFechaActualizacion(LocalDateTime.now());
        return carritoMapper.toDto(carritoRepository.save(carrito));
    }

    public CarritoResponseDTO actualizarCantidadItem(Long carritoId, Long itemId, Integer cantidad) {
        if (cantidad == null || cantidad < 1) {
            throw new ArgumentInvalidException("La cantidad debe ser al menos 1");
        }

        Carrito carrito = getCarritoEntityById(carritoId);
        Item item = getItemEntityById(carrito, itemId);
        item.setCantidad(cantidad);
        carrito.setFechaActualizacion(LocalDateTime.now());
        return carritoMapper.toDto(carritoRepository.save(carrito));
    }

    public CarritoResponseDTO eliminarItem(Long carritoId, Long itemId) {
        Carrito carrito = getCarritoEntityById(carritoId);

        boolean removed = carrito.getItems().removeIf(item -> item.getId().equals(itemId));

        if (!removed) {
            throw new ResourceNotFoundException("El item con id " + itemId + " no está en el carrito");
        }

        carrito.setFechaActualizacion(LocalDateTime.now());
        return carritoMapper.toDto(carritoRepository.save(carrito));
    }

   public CarritoResponseDTO eliminarItemPorProducto(Long carritoId, Long productoId) {
    Carrito carrito = getCarritoEntityById(carritoId);

    // Buscamos el ítem que corresponde al productoId
    Item item = carrito.getItems().stream()
            .filter(i -> i.getProducto().getId().equals(productoId))
            .findFirst()
            .orElseThrow(() -> new ResourceNotFoundException("El producto con id " + productoId + " no está en el carrito"));

    // Lógica: si hay más de 1, restamos 1. Si es 1, eliminamos el ítem.
    if (item.getCantidad() > 1) {
        item.setCantidad(item.getCantidad() - 1);
    } else {
        carrito.getItems().remove(item);
    }

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
                .mapToDouble(item -> item.getCantidad() * item.getPrecioUnitario())
                .sum();
    }

    public PedidoResponseDTO pagar(Long carritoId) {
        Carrito carrito = getCarritoEntityById(carritoId);

        if (carrito.getItems().isEmpty()) {
            throw new ArgumentInvalidException("El carrito está vacío, no se puede generar un pedido");
        }

        // 1) Antes de generar el pedido validamos que TODOS los productos tengan
        //    stock suficiente. Si alguno no alcanza, cortamos y no se descuenta nada.
        for (Item carritoItem : carrito.getItems()) {
            Producto producto = carritoItem.getProducto();
            if (producto.getStock() < carritoItem.getCantidad()) {
                throw new ArgumentInvalidException(
                        "No hay stock suficiente para el producto: " + producto.getNombre()
                        + " (disponible: " + producto.getStock()
                        + ", solicitado: " + carritoItem.getCantidad() + ")");
            }
        }

        Pedido pedido = new Pedido();
        pedido.setUsuario(carrito.getUsuario());
        pedido.setItems(new ArrayList<>());
        for (Item carritoItem : new ArrayList<>(carrito.getItems())) {
            Item pedidoItem = new Item();
            pedidoItem.setProducto(carritoItem.getProducto());
            pedidoItem.setCantidad(carritoItem.getCantidad());
            pedidoItem.setPrecioUnitario(carritoItem.getPrecioUnitario());
            pedidoItem.setPedido(pedido);
            pedido.getItems().add(pedidoItem);

            // 2) Descontamos el stock del producto (checkout sin pago real).
            Producto producto = carritoItem.getProducto();
            producto.setStock(producto.getStock() - carritoItem.getCantidad());
            productoRepository.save(producto);
        }
        pedido.setPrecioTotal(calcularTotal(carrito));
        pedido.setFechaPedido(LocalDateTime.now());
        pedido.setEstado("pendiente");

        Pedido savedPedido = pedidoRepository.save(pedido);

        carrito.getItems().clear();
        carrito.setFechaActualizacion(LocalDateTime.now());
        carritoRepository.save(carrito);

        return pedidoMapper.toDto(savedPedido);
    }

    public void deleteCarrito(Long carritoId) {
        carritoRepository.deleteById(carritoId);
    }

    private Carrito getCarritoEntityById(Long id) {
        return carritoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Carrito no encontrado con id: " + id));
    }

    private Item getItemEntityById(Carrito carrito, Long itemId) {
        return carrito.getItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("El item con id " + itemId + " no está en el carrito"));
    }

    private Double calcularTotal(Carrito carrito) {
        return carrito.getItems().stream()
                .mapToDouble(item -> item.getCantidad() * item.getPrecioUnitario())
                .sum();
    }
}

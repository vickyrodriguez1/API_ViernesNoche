package com.uade.tpo.e_commerce3.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uade.tpo.e_commerce3.model.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {

    // Borra todos los items (de carritos o pedidos) que referencian a un producto.
    // Lo usamos antes de eliminar un producto para no violar la foreign key.
    void deleteByProductoId(Long productoId);
}

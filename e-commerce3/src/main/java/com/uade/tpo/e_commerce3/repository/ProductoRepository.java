package com.uade.tpo.e_commerce3.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uade.tpo.e_commerce3.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}

package com.uade.tpo.e_commerce3.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import java.util.ArrayList;

@Data
@Entity
@Table(name = "roles")
public class Rol {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)    
    private String nombre;

    
    @OneToMany(mappedBy = "rol")
    private List<Usuario> usuarios = new ArrayList<>();
}

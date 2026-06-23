package com.uade.tpo.e_commerce3.dto;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProductoListDTO {

    private Long id;
    private String nombre;
    private Double precio;

    // Sumamos stock y categorias para que el catalogo (listado) pueda mostrar
    // si el producto esta agotado y filtrar por categoria sin entrar al detalle.
    private Integer stock;
    private List<String> categorias;

    @JsonProperty("imagen_base64")
    private String imagenBase64;
}

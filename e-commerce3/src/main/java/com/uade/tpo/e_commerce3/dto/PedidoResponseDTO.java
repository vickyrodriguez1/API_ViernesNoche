package com.uade.tpo.e_commerce3.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoResponseDTO {
    private Long id;
    private Long usuarioId;
    private Long productoId;
    private String productoNombre;
    private Integer cantidad;
    private Double precioTotal;
    private LocalDateTime fechaPedido;
    private LocalDateTime fechaEntrega;
    private String estado;
}

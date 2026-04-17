package com.uade.tpo.e_commerce3.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoResponseDTO {
    private Long id;
    private Long usuarioId;
    private List<ProductoListDTO> productos;
    private Double precioTotal;
    private LocalDateTime fechaPedido;
    private LocalDateTime fechaEntrega;
    private String estado;
}

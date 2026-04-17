package com.uade.tpo.e_commerce3.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class PedidoUpdateDTO {
    private String estado;
    private LocalDateTime fechaEntrega;
}

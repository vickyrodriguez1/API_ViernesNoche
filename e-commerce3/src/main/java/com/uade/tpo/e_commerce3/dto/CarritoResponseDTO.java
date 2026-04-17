package com.uade.tpo.e_commerce3.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarritoResponseDTO {
    private Long id;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private List<ItemCarritoResponseDTO> items;
    private Double total;
}

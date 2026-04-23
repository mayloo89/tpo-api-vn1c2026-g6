package com.uade.tpo.e_commerce3.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductoRequest {
    private String nombre;
    private String descripcion;
    private Double precio;
    private Integer stock;
}

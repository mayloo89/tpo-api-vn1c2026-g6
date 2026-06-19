package com.uade.tpo.e_commerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductoRequestDTO {
    private String nombre;
    private String descripcion;
    private Double precio;
    private Integer stock;
    private String imagen;
}
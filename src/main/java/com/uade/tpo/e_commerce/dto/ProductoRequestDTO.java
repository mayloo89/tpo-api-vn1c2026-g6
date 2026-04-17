package com.uade.tpo.e_commerce.dto;

import lombok.Data;

/**
 * DTO (Data Transfer Object) para representar la información de un producto en las solicitudes de la API.
 * Este DTO se utiliza para recibir los datos necesarios para crear o actualizar un producto, evitando exponer toda la entidad del modelo y mejorando la seguridad y el rendimiento.
 */
@Data
public class ProductoRequestDTO {
    private String nombre;
    private String descripcion;
    private Double precio;
    private Integer stock;
}

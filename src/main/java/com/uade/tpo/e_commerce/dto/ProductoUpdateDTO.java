package com.uade.tpo.e_commerce.dto;

import lombok.Data;

/**
 * DTO (Data Transfer Object) para representar la información de un producto en las solicitudes de actualización de la API.
 * Este DTO se utiliza para recibir los datos necesarios para actualizar un producto, permitiendo modificar solo ciertos campos como el precio y el stock, sin necesidad de enviar toda la información del producto.
 */
@Data
public class ProductoUpdateDTO {
    private Double precio;
    private Integer stock;
}

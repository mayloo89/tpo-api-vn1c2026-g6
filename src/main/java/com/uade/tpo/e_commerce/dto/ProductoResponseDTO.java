package com.uade.tpo.e_commerce.dto;

import lombok.Data;

/** 
 * DTO (Data Transfer Object) para representar la información de un producto en las respuestas de la API.
 * Este DTO se utiliza para enviar solo los datos relevantes del producto al cliente, evitando exponer información sensible o innecesaria.
*/
@Data
public class ProductoResponseDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private Double precio;
    private Integer stock;
    
    public ProductoResponseDTO(Long id, String nombre, String descripcion, Double precio, Integer stock) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
    }

}

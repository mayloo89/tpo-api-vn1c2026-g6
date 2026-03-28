package com.uade.tpo.e_commerce.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/*
* En model se definen las entidades que representan las tablas de la base de datos. 
* En este caso, la clase Producto representa la tabla "productos" en la base de datos.
*/

/**
 * Entidad que representa un producto en el e-commerce.
 * Mapea a la tabla "productos" en la base de datos.
 * Utiliza Lombok para generar automáticamente getters, setters, toString, equals y hashCode.
 */
@Data // Genera automáticamente getters, setters, toString, equals y hashCode
@Entity // Marca esta clase como una entidad persistente de JPA
@Table(name = "productos") // Define el nombre de la tabla en la base de datos
public class Producto {
    /**
     * Identificador único del producto.
     * Auto-generado por la base de datos.
     */
    @Id // Marca este campo como la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incrementa el ID usando la estrategia IDENTITY
    private Long id;

    /**
     * Nombre del producto.
     * Campo obligatorio que no puede ser nulo.
     */
    @Column(nullable = false) // Especifica que este campo no puede ser nulo en la base de datos
    private String nombre;

    /**
     * Descripción detallada del producto.
     * Campo opcional que puede ser nulo.
     */
    private String descripcion;

    /**
     * Precio del producto.
     * Campo obligatorio que no puede ser nulo.
     */
    @Column(nullable = false)
    private Double precio;

    /**
     * Cantidad en stock del producto.
     * Campo obligatorio que no puede ser nulo.
     */
    @Column(nullable = false)
    private Integer stock;
}

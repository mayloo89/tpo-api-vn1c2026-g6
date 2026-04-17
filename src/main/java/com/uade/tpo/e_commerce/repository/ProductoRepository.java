package com.uade.tpo.e_commerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.uade.tpo.e_commerce.model.Producto;

/*
* En repository se definen las interfaces que extienden JpaRepository para cada entidad.
* Estas interfaces permiten realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar) sin necesidad de escribir código SQL.
*/

/**
 * Repositorio para la entidad Producto.
 * Proporciona operaciones CRUD automáticas a través de JpaRepository.
 * 
 * @param <Producto> la entidad que gestiona (Producto)
 * @param <Long> el tipo del identificador único (ID) de la entidad
 */
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByNombreContaining(String nombre); // Método de consulta personalizada para buscar productos por nombre (opcional)

    @Query("SELECT p FROM Producto p WHERE p.precio >= :minPrecio AND p.precio <= :maxPrecio") // Consulta personalizada para buscar productos por rango de precio (opcional)
    List<Producto> findByPrecioBetween(Double minPrecio, Double maxPrecio);

}

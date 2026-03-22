package com.uade.tpo.e_commerce.repository;

import com.uade.tpo.e_commerce.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

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
}

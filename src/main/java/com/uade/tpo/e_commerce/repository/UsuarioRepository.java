package com.uade.tpo.e_commerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uade.tpo.e_commerce.model.Usuario;


/**
 * Repositorio para manejar operaciones CRUD de la entidad Usuario.
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Busca un usuario por email; Spring Data genera la consulta automáticamente.
    Optional<Usuario> findByEmail(String email);
    
    // Verifica si ya existe un usuario con ese email.
    Boolean existsByEmail(String email);
}

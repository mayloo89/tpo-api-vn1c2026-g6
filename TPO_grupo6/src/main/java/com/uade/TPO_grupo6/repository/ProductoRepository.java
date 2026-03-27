package com.uade.TPO_grupo6.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.uade.TPO_grupo6.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
}

package com.uade.tpo.e_commerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uade.tpo.e_commerce.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    
}

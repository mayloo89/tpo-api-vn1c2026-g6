package com.uade.tpo.e_commerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uade.tpo.e_commerce.model.Pedido;
import com.uade.tpo.e_commerce.model.PedidoEstado;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
	Optional<Pedido> findByUsuarioIdAndEstado(Long usuarioId, PedidoEstado estado);
}

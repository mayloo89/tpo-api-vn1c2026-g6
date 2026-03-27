package com.uade.TPO_grupo6.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.TPO_grupo6.model.Pedido;
import com.uade.TPO_grupo6.repository.PedidoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional

public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    /**
     * Devuelve la lista completa de pedidos.
     */
    public List<Pedido> getAllPedidos() {
        return pedidoRepository.findAll();
    }

    /**
     * Busca un pedido por su id.
     */
    public Pedido getPedidoById(Long id) {
        if (id == null) {
            return null;
        }
        return pedidoRepository.findById(id).orElse(null);
    }

    /**
     * Crea un nuevo pedido.
     */
    public Pedido addPedido(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }
}


package com.uade.tpo.e_commerce.service;

import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.e_commerce.exception.PedidoNotFoundException;
import com.uade.tpo.e_commerce.model.Pedido;
import com.uade.tpo.e_commerce.repository.PedidoRepository;

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
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoNotFoundException("Pedido no encontrado"));
    }

    /**
     * Crea un nuevo pedido.
     */
    public Pedido addPedido(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    /**
     * Elimina un pedido por su id.
     * Si el pedido no existe, no hace nada.
     */
    public void deletePedido(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }

        if (!pedidoRepository.existsById(id)) {
            throw new PedidoNotFoundException("Pedido no existe");
        }

        pedidoRepository.deleteById(id);
    }

    /**
     * Actualiza los datos de un pedido existente.
     */
    public Pedido updatePedido(Long id, Pedido pedido) {
        Pedido existingPedido = getPedidoById(id);
        existingPedido.setDescripcion(pedido.getDescripcion());
        existingPedido.setCantidad(pedido.getCantidad());
        return pedidoRepository.save(existingPedido);
    }
}


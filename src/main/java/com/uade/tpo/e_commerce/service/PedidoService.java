package com.uade.tpo.e_commerce.service;

import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.e_commerce.dto.PedidoRequestDTO;
import com.uade.tpo.e_commerce.dto.PedidoResponseDTO;
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
    public List<PedidoResponseDTO> getAllPedidos() {
        return pedidoRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Busca un pedido por su id.
     */
    public PedidoResponseDTO getPedidoById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El id del pedido no puede ser nulo");
        }
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoNotFoundException("Pedido no encontrado con id: " + id));
        return toResponse(pedido);
    }

    /**
     * Crea un nuevo pedido.
     */
    public PedidoResponseDTO addPedido(PedidoRequestDTO pedidoDTO) {
        if (pedidoDTO.getCantidad() == null || pedidoDTO.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }
        if (pedidoDTO.getDescripcion() == null || pedidoDTO.getDescripcion().isBlank()) {
            throw new IllegalArgumentException("La descripcion no puede ser vacia");
        }

        Pedido pedido = new Pedido();
        pedido.setDescripcion(pedidoDTO.getDescripcion());
        pedido.setCantidad(pedidoDTO.getCantidad());

        Pedido savedPedido = pedidoRepository.save(pedido);
        return toResponse(savedPedido);
    }

    public PedidoResponseDTO updatePedido(Long id, PedidoRequestDTO pedidoDTO) {
        Pedido existingPedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoNotFoundException("Pedido no encontrado con id: " + id));

        if (pedidoDTO.getCantidad() == null || pedidoDTO.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }
        if (pedidoDTO.getDescripcion() == null || pedidoDTO.getDescripcion().isBlank()) {
            throw new IllegalArgumentException("La descripcion no puede ser vacia");
        }

        existingPedido.setDescripcion(pedidoDTO.getDescripcion());
        existingPedido.setCantidad(pedidoDTO.getCantidad());

        Pedido updatedPedido = pedidoRepository.save(existingPedido);
        return toResponse(updatedPedido);
    }

    /**
     * Elimina un pedido por su id.
     */
    public void deletePedido(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El id del pedido no puede ser nulo");
        }
        if (!pedidoRepository.existsById(id)) {
            throw new PedidoNotFoundException("Pedido no encontrado con id: " + id);
        }
        pedidoRepository.deleteById(id);
    }

    private PedidoResponseDTO toResponse(Pedido pedido) {
        return new PedidoResponseDTO(
                pedido.getId(),
                pedido.getDescripcion(),
                pedido.getCantidad());
    }
}


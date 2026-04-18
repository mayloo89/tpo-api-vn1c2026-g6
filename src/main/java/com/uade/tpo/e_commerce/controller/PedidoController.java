package com.uade.tpo.e_commerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.e_commerce.dto.PedidoRequestDTO;
import com.uade.tpo.e_commerce.dto.PedidoResponseDTO;
import com.uade.tpo.e_commerce.service.PedidoService;

@RestController
@RequestMapping("/api/pedidos")

public class PedidoController {
    
    @Autowired 
    private PedidoService pedidoService;

    /**
     * Endpoint para obtener todos los pedidos.
     * GET /api/pedidos
     */
    @GetMapping
    public List<PedidoResponseDTO> getAllPedidos() {
        return pedidoService.getAllPedidos();
    }

    /**
     * Endpoint para obtener un pedido por su id.
     * GET /api/pedidos/{id}
     */
    @GetMapping("/{id}")
    public PedidoResponseDTO getPedidoById(@PathVariable Long id) {
        return pedidoService.getPedidoById(id);
    }

    /**
     * Endpoint para crear un nuevo pedido.
     * POST /api/pedidos
     */
    @PostMapping
    public PedidoResponseDTO addPedido(@RequestBody PedidoRequestDTO pedidoDTO) {
        return pedidoService.addPedido(pedidoDTO);
    }

    /**
     * Endpoint para eliminar un pedido por su id.
     * DELETE /api/pedidos/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePedido(@PathVariable Long id) {
        pedidoService.deletePedido(id);
        return ResponseEntity.noContent().build();
    }

    /** 
     * Endpoint para actualizar un pedido existente.
     * PUT /api/pedidos/{id}
     */
    @PutMapping("/{id}")
    public PedidoResponseDTO updatePedido(@PathVariable Long id, @RequestBody PedidoRequestDTO pedidoDTO) {
        return pedidoService.updatePedido(id, pedidoDTO);
    }
}

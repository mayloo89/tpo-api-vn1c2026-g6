package com.uade.TPO_grupo6.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.TPO_grupo6.model.Pedido;
import com.uade.TPO_grupo6.service.PedidoService;

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
    public List<Pedido> getAllPedidos() {
        return pedidoService.getAllPedidos();
    }

    /**
     * Endpoint para obtener un pedido por su id.
     * GET /api/pedidos/{id}
     */
    @GetMapping("/{id}")
    public Pedido getPedidoById(@PathVariable Long id) {
        return pedidoService.getPedidoById(id);
    }

    /**
     * Endpoint para crear un nuevo pedido.
     * POST /api/pedidos
     */
    @PostMapping
    public Pedido addPedido(@RequestBody Pedido pedido) {
        return pedidoService.addPedido(pedido);
    }

}

package com.uade.tpo.e_commerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.e_commerce.model.Pedido;
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

    /**
     * Endpoint para eliminar un pedido por su id.
     * DELETE /api/pedidos/{id}
     */
    @DeleteMapping("/{id}")
    public void deletePedido(@PathVariable Long id) {
        pedidoService.deletePedido(id);
    }

    /** 
     * Endpoint para actualizar un pedido existente.
     * PUT /api/pedidos/{id}
     */
    @PutMapping("/{id}")
    public Pedido updatePedido(@PathVariable Long id, @RequestBody Pedido pedido) {
        // Para actualizar un pedido, primero obtenemos el pedido existente por su id
        Pedido existingPedido = pedidoService.getPedidoById(id);
        if (existingPedido == null) {
            return null; // Si el pedido no existe, retornamos null
        }

        // Actualizamos los campos del pedido existente con los datos del pedido recibido en la solicitud
        existingPedido.setDescripcion(pedido.getDescripcion());
        existingPedido.setCantidad(pedido.getCantidad());

        // Guardamos el pedido actualizado en la base de datos
        return pedidoService.addPedido(existingPedido);
    }
}

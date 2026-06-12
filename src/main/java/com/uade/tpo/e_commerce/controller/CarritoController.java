package com.uade.tpo.e_commerce.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.e_commerce.dto.PedidoItemRequestDTO;
import com.uade.tpo.e_commerce.dto.PedidoResponseDTO;
import com.uade.tpo.e_commerce.service.CarritoService;

@RestController
@RequestMapping("/api/carrito")
public class CarritoController {

    private final CarritoService carritoService;

    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    @GetMapping
    public PedidoResponseDTO getCarrito(Authentication authentication) {
        return carritoService.getCarrito(authentication);
    }

    @PostMapping("/items")
    public PedidoResponseDTO addItem(Authentication authentication, @RequestBody PedidoItemRequestDTO request) {
        return carritoService.addItem(authentication, request);
    }

    @PutMapping("/items/{productoId}")
    public PedidoResponseDTO updateItem(Authentication authentication, @PathVariable Long productoId,
            @RequestParam Integer cantidad) {
        return carritoService.updateItem(authentication, productoId, cantidad);
    }

    @DeleteMapping("/items/{productoId}")
    public PedidoResponseDTO removeItem(Authentication authentication, @PathVariable Long productoId) {
        return carritoService.removeItem(authentication, productoId);
    }

    @DeleteMapping("/clear")
    public void clear(Authentication authentication) {
        carritoService.clear(authentication);
    }
}
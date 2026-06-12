package com.uade.tpo.e_commerce.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.e_commerce.dto.ProductoResponseDTO;
import com.uade.tpo.e_commerce.service.FavoritoService;

@RestController
@RequestMapping("/api/favoritos")
public class FavoritoController {

    private final FavoritoService favoritoService;

    public FavoritoController(FavoritoService favoritoService) {
        this.favoritoService = favoritoService;
    }

    @GetMapping
    public List<ProductoResponseDTO> getFavoritos(Authentication authentication) {
        return favoritoService.getFavoritos(authentication);
    }

    @PostMapping("/{productoId}")
    public List<ProductoResponseDTO> addFavorito(Authentication authentication, @PathVariable Long productoId) {
        return favoritoService.addFavorito(authentication, productoId);
    }

    @DeleteMapping("/{productoId}")
    public List<ProductoResponseDTO> removeFavorito(Authentication authentication, @PathVariable Long productoId) {
        return favoritoService.removeFavorito(authentication, productoId);
    }
}
package com.uade.tpo.e_commerce3.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.e_commerce3.dto.CategoriaRequest;
import com.uade.tpo.e_commerce3.dto.CategoriaResponse;
import com.uade.tpo.e_commerce3.service.CategoriaService;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {
    
    @Autowired
    public CategoriaService categoriaService;

    @GetMapping
    public List<CategoriaResponse> getAllCategorias() {
        return categoriaService.getAllCategorias();
    }

    @GetMapping("/{id}")
    public CategoriaResponse getCategoriaById(@PathVariable Long id) {
        return categoriaService.getCategoriaById(id);
    }

    @PostMapping
    public CategoriaResponse saveCategoria(@RequestBody CategoriaRequest categoriaDTO) {
        CategoriaResponse savedCategoria = categoriaService.saveCategoria(categoriaDTO);
        return savedCategoria; 
    }

    @PutMapping("/{id}")
    public CategoriaResponse updateCategoria(@PathVariable Long id, @RequestBody CategoriaRequest categoriaDTO) {
        return categoriaService.updateCategoria(id, categoriaDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoriaById(@PathVariable Long id) {
        categoriaService.deleteCategoriaById(id);
        return ResponseEntity.noContent().build();
    }
}

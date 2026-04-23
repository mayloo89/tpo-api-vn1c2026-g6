package com.uade.tpo.e_commerce3.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.e_commerce3.dto.CategoriaRequest;
import com.uade.tpo.e_commerce3.dto.CategoriaResponse;
import com.uade.tpo.e_commerce3.exception.CategoriaNotFoundException;
import com.uade.tpo.e_commerce3.model.Categoria;
import com.uade.tpo.e_commerce3.repository.CategoriaRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CategoriaService {
    
    @Autowired
    public CategoriaRepository categoriaRepository;

    public List<CategoriaResponse> getAllCategorias() {
        return categoriaRepository.findAll().stream()
                .map(categoria -> new CategoriaResponse(
                        categoria.getId(),
                        categoria.getNombre()))
                .toList();
    }

    public CategoriaResponse getCategoriaById(Long id) {
        Categoria categoria = categoriaRepository.findById(id).orElse(null);
        if (categoria == null) {
            throw new CategoriaNotFoundException(id);
        }

        CategoriaResponse categoriaResponse = new CategoriaResponse(
                categoria.getId(),
                categoria.getNombre());
        
        return categoriaResponse;
    }

    public CategoriaResponse saveCategoria(CategoriaRequest categoriaDTO) {
        if(categoriaDTO.getNombre() == null || categoriaDTO.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoria no puede ser nulo o vacío");

        }

        Categoria categoria = Categoria.builder()
                .nombre(categoriaDTO.getNombre())
                .build();
        
        Categoria savedCategoria = categoriaRepository.save(categoria);
        CategoriaResponse categoriaResponse = new CategoriaResponse(
            savedCategoria.getId(),
            savedCategoria.getNombre());
                    
        return categoriaResponse;
    }

    public CategoriaResponse updateCategoria(Long id, CategoriaRequest categoriaDTO) {
        Categoria categoria = categoriaRepository.findById(id).orElse(null);
        if (categoria == null) {
            throw new CategoriaNotFoundException(id);
        }
        if(categoriaDTO.getNombre() == null || categoriaDTO.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoria no puede ser nulo o vacío");
        }

        categoria.setNombre(categoriaDTO.getNombre());

        Categoria updatedCategoria = categoriaRepository.save(categoria);

        CategoriaResponse categoriaResponse = new CategoriaResponse(
            updatedCategoria.getId(),
            updatedCategoria.getNombre());
                    
        return categoriaResponse;
    }

    public void deleteCategoriaById(Long id) {
        Categoria categoria = categoriaRepository.findById(id).orElse(null);
        if (categoria == null) {
            throw new CategoriaNotFoundException(id);
        }
        categoriaRepository.deleteById(id);
    }
}

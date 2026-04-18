package com.uade.tpo.e_commerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.e_commerce.dto.CategoriaRequestDTO;
import com.uade.tpo.e_commerce.dto.CategoriaResponseDTO;
import com.uade.tpo.e_commerce.exception.CategoriaNotFoundException;
import com.uade.tpo.e_commerce.model.Categoria;
import com.uade.tpo.e_commerce.repository.CategoriaRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CategoriaService {
    
    @Autowired
    public CategoriaRepository categoriaRepository;

    public List<CategoriaResponseDTO> getAllCategorias() {
        return categoriaRepository.findAll().stream()
                .map(categoria -> new CategoriaResponseDTO(
                        categoria.getId(),
                        categoria.getNombre()))
                .toList();
    }

    public CategoriaResponseDTO getCategoriaById(Long id) {
        Categoria categoria = categoriaRepository.findById(id).orElse(null);
        if (categoria == null) {
            throw new CategoriaNotFoundException(id);
        }

        CategoriaResponseDTO categoriaResponse = new CategoriaResponseDTO(
                categoria.getId(),
                categoria.getNombre());
        
        return categoriaResponse;
    }

    public CategoriaResponseDTO saveCategoria(CategoriaRequestDTO categoriaDTO) {
        if(categoriaDTO.getNombre() == null || categoriaDTO.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoria no puede ser nulo o vacío");

        }

        Categoria categoria = Categoria.builder()
                .nombre(categoriaDTO.getNombre())
                .build();
        
        Categoria savedCategoria = categoriaRepository.save(categoria);
        CategoriaResponseDTO categoriaResponse = new CategoriaResponseDTO(
            savedCategoria.getId(),
            savedCategoria.getNombre());
                    
        return categoriaResponse;
    }

    public CategoriaResponseDTO updateCategoria(Long id, CategoriaRequestDTO categoriaDTO) {
        Categoria categoria = categoriaRepository.findById(id).orElse(null);
        if (categoria == null) {
            throw new CategoriaNotFoundException(id);
        }
        if(categoriaDTO.getNombre() == null || categoriaDTO.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoria no puede ser nulo o vacío");
        }

        categoria.setNombre(categoriaDTO.getNombre());

        Categoria updatedCategoria = categoriaRepository.save(categoria);

        CategoriaResponseDTO categoriaResponse = new CategoriaResponseDTO(
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

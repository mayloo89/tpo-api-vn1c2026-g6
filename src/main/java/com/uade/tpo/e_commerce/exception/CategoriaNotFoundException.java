package com.uade.tpo.e_commerce.exception;

public class CategoriaNotFoundException extends RuntimeException {
    public CategoriaNotFoundException(Long id) {
        super("No se encontro la Categoria con el id: " + id);
    }
    
}

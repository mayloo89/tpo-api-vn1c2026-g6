package com.uade.tpo.e_commerce.exception;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String email) {
        super("El email ya existe: " + email);
    }
    
}

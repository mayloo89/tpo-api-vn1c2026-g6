package com.uade.tpo.e_commerce.exception;

/**
 * Excepción de negocio para indicar que un usuario no fue encontrado.
 *
 * Se utiliza en la capa de servicio cuando se solicita un recurso inexistente,
 * y luego es traducida a HTTP 404 por el manejador global de excepciones.
 */
public class UsuarioNotFoundException extends RuntimeException {

    /**
     * Crea una excepción con un mensaje descriptivo.
     *
     * @param message detalle del error para devolver al cliente
     */
    public UsuarioNotFoundException(String message) {
        super(message);
    }
}
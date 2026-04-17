package com.uade.tpo.e_commerce.exception;

/**
 * Excepción de negocio para indicar que un producto no fue encontrado.
 *
 * Se utiliza en la capa de servicio cuando se solicita un recurso inexistente,
 * y luego es traducida a HTTP 404 por el manejador global de excepciones.
 */
public class ProductoNotFoundException extends RuntimeException {

    /**
     * Crea una excepción con un mensaje descriptivo.
     *
     * @param message detalle del error para devolver al cliente
     */
    public ProductoNotFoundException(String message) {
        super(message);
    }
}

package com.uade.tpo.e_commerce.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*
 * En exception se definen clases para centralizar el manejo de errores de la API.
 * Estas clases permiten devolver respuestas HTTP consistentes ante fallos de negocio o validación.
 */

/**
 * Manejador global de excepciones para toda la aplicación.
 *
 * Captura excepciones lanzadas por controllers y services, y las transforma
 * en respuestas HTTP claras para el cliente.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja errores cuando un producto no existe.
     *
     * @param ex excepción de producto no encontrado
     * @return respuesta HTTP 404 con el detalle del error
     */
    @ExceptionHandler(ProductoNotFoundException.class)
    public ResponseEntity<String> handleNotFound(ProductoNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    /**
     * Maneja errores cuando un pedido no existe.
     *
     * @param ex excepción de pedido no encontrado
     * @return respuesta HTTP 404 con el detalle del error
     */
    @ExceptionHandler(PedidoNotFoundException.class)
    public ResponseEntity<String> handleNotFound(PedidoNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    /**
     * Maneja errores cuando un usuario no existe.
     *
     * @param ex excepción de usuario no encontrado
     * @return respuesta HTTP 404 con el detalle del error
     */
    @ExceptionHandler(UsuarioNotFoundException.class)
    public ResponseEntity<String> handleNotFound(UsuarioNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    /**
     * Maneja errores de ejecución no controlados específicamente.
     *
     * @param ex excepción de runtime
     * @return respuesta HTTP 400 con el detalle del error
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntime(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }
}
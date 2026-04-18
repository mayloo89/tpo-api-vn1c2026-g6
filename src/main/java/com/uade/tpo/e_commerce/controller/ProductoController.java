package com.uade.tpo.e_commerce.controller;

import java.util.List;

import com.uade.tpo.e_commerce.dto.ProductoRequestDTO;
import com.uade.tpo.e_commerce.dto.ProductoResponseDTO;
import com.uade.tpo.e_commerce.dto.ProductoUpdateDTO;
import com.uade.tpo.e_commerce.service.ProductoService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST que maneja las operaciones CRUD para productos.
 * Expone endpoints en /api/productos para gestionar la información de productos.
 */
@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    /**
     * Obtiene todos los productos disponibles.
     *
     * @return lista de todos los productos
     */
    @GetMapping
    public List<ProductoResponseDTO> getAllProductos() {
        return productoService.getAllProductos();
    }

    /**
     * Obtiene un producto específico por su ID.
     *
     * @param id identificador del producto
     * @return el producto encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> getProductoById(@PathVariable Long id) {
        ProductoResponseDTO productoResponse = productoService.getProductoById(id);
        return new ResponseEntity<>(productoResponse, HttpStatus.OK);
    }

    /**
     * Crea un nuevo producto en la base de datos.
     *
     * @param productoRequest producto a crear, recibido en el body de la solicitud
     * @return producto creado con status 201
     */
    @PostMapping
    public ResponseEntity<ProductoResponseDTO> saveProducto(@RequestBody ProductoRequestDTO productoRequest) {
        ProductoResponseDTO savedProducto = productoService.saveProducto(productoRequest);
        return new ResponseEntity<>(savedProducto, HttpStatus.CREATED);
    }

    /**
     * Elimina un producto por su ID.
     *
     * @param id identificador del producto
     * @return respuesta sin contenido (204)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductoById(@PathVariable Long id) {
        productoService.deleteProductoById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Actualiza precio y stock de un producto existente.
     *
     * @param id identificador del producto
     * @param productoUpdateDTO datos de actualización del producto
     * @return producto actualizado con status 200
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> updateProducto(@PathVariable Long id, @RequestBody ProductoUpdateDTO productoUpdateDTO) {
        ProductoResponseDTO updatedProducto = productoService.updateProducto(id, productoUpdateDTO);
        return new ResponseEntity<>(updatedProducto, HttpStatus.OK);
    }
}
    


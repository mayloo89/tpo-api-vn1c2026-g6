package com.uade.TPO_grupo6.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.TPO_grupo6.model.Producto;
import com.uade.TPO_grupo6.service.ProductoService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/productos")

public class ProductoController {
    
    @Autowired
    private ProductoService productoService;

    /**
     * Endpoint para obtener todos los productos.
     * GET /api/productos
     */
    @GetMapping
    public List<Producto> getAllProductos() {
        return productoService.getAllProductos();
    }

    /**
     * Endpoint para obtener un producto por su id.
     * GET /api/productos/{id}
     */
    @GetMapping("/{id}")
    public Producto getProductoById(@PathVariable Long id) {
        return productoService.getProductoById(id);
    }

    /**
     * Endpoint para crear un nuevo producto.
     * POST /api/productos
     */
    @PostMapping
    public Producto addProducto(@RequestBody Producto producto) {
        return productoService.addProducto(producto);
    }

    /**
     * Endpoint para eliminar un producto.
     * DELETE /api/productos/{id}
     */
    @DeleteMapping("/{id}")
    public void deleteProducto(@PathVariable Long id) {
        productoService.deleteProducto(id);
    }

    /**
     * Endpoint para actualizar un producto existente.
     * PUT /api/productos/{id}
     */
    @PutMapping("/{id}")
    public Producto updateProducto(@PathVariable Long id, @RequestBody Producto producto) {
        // Para actualizar, primero obtenemos el producto existente
        Producto existingProducto = productoService.getProductoById(id);
        if (existingProducto == null) {
            return null; // O lanzar una excepción si prefieres
        }
        // Actualizamos los campos del producto existente con los nuevos valores
        existingProducto.setNombre(producto.getNombre());
        existingProducto.setDescripcion(producto.getDescripcion());
        existingProducto.setPrecio(producto.getPrecio());
        existingProducto.setStock(producto.getStock());
        // Guardamos el producto actualizado
        return productoService.addProducto(existingProducto);
    }
}

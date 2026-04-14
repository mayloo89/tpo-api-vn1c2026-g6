package com.uade.tpo.e_commerce.controller;

import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.uade.tpo.e_commerce.dto.ProductoDTO;
import com.uade.tpo.e_commerce.model.Producto;
import com.uade.tpo.e_commerce.service.ProductoService;

/*
 * En controller se definen las clases que manejan las solicitudes HTTP y devuelven respuestas HTTP.
 * Estas clases interactúan con los services para realizar operaciones sobre los datos y devolver los resultados al cliente.
 */

/**
 * Controlador REST que maneja las operaciones CRUD para productos.
 * Expone endpoints en /api/productos para gestionar la información de productos.
 */
@RestController // Marca esta clase como un controlador REST que maneja solicitudes HTTP y devuelve respuestas en formato JSON
@RequestMapping("/api/productos") // Define la ruta base para todos los endpoints de este controlador
public class ProductoController {
    
    @Autowired // Inyecta automáticamente una instancia de ProductoService desde el contenedor de Spring
    private ProductoService productoService;

    /**
     * Obtiene todos los productos disponibles.
     *
     * @return lista de todos los productos
     */
    @GetMapping // Maneja solicitudes GET a la ruta base /api/productos (definida por @RequestMapping)
    public List<Producto> getAllProductos() {
        return productoService.getAllProductos();
    }

    /**
     * Obtiene un producto específico por su ID.
     *
     * @param id identificador del producto
     * @return el producto encontrado
     *
    @GetMapping("/{id}") // Maneja solicitudes GET a la ruta /api/productos/{id}, donde {id} es un parámetro dinámico
    // Extrae el valor de {id} de la ruta y lo pasa como parámetro al método
    public Producto getProductoById(@PathVariable Long id) {
        return productoService.getProductoById(id);
    }*/

    /**
     * Crea un nuevo producto en la base de datos.
     *
     * @param producto datos del producto a crear
     * @return el producto creado con su ID asignado
     *
    @PostMapping // Maneja solicitudes POST a la ruta /api/productos
    // Lee el JSON del cuerpo de la solicitud y lo convierte en un objeto Producto
    public Producto saveProducto(@RequestBody Producto producto) {
        return productoService.saveProducto(producto);
    }*/

    /**
     * Endpoint para eliminar un producto.
     * DELETE /api/productos/{id}
     */
    @DeleteMapping("/{id}")
    public void deleteProducto(@PathVariable Long id) {
        productoService.deleteProductoById(id);
    }   


    /**
     * Endpoint para actualizar un producto existente.
     * PUT /api/productos/{id}
     *
    @PutMapping("/{id}")
    public Producto updateProducto(@PathVariable Long id, @RequestBody Producto producto) {
        // Para actualizar, primero obtenemos el producto existente
        Producto existingProducto = productoService.getProductoById(id);
        if (existingProducto == null) {
            return null;
        }
        // Actualizamos los campos del producto existente con los nuevos valores
        existingProducto.setNombre(producto.getNombre());
        existingProducto.setDescripcion(producto.getDescripcion());
        existingProducto.setPrecio(producto.getPrecio());
        existingProducto.setStock(producto.getStock());
        // Guardamos el producto actualizado
        return productoService.saveProducto(existingProducto);
    }*/

        // DTO: Data Transfer Object, es una clase que se utiliza para transferir datos entre capas de la aplicación, en este caso entre el controlador y el servicio. El DTO suele contener solo los campos necesarios para la operación específica, lo que ayuda a evitar exponer toda la entidad del modelo y a mejorar la seguridad y el rendimiento.

     @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> getProductoByID(@PathVariable Long id) {
        @Nullable ProductoDTO productoDTO = productoService.getProductoById(id);
        if (productoDTO == null) {
            return ResponseEntity.notFound().build(); // Devuelve 404 si el producto no existe
        }

        return new ResponseEntity<ProductoDTO>(productoDTO, HttpStatus.OK); // Devuelve 200 OK con el DTO del producto
    }

    @PostMapping
    public ResponseEntity<ProductoDTO> saveProducto(@RequestBody ProductoDTO productoDTO) {
        ProductoDTO savedProducto = productoService.saveProducto(productoDTO);
        return new ResponseEntity<>(savedProducto, HttpStatus.CREATED); // Devuelve 201 Created con el DTO del producto creado
    }

    @PutMapping("/{id}")
    public ProductoDTO updateProducto(@PathVariable Long id, @RequestBody ProductoDTO productoDTO) {
        ProductoDTO updatedProducto = productoService.updateProducto1(id, productoDTO);
        if (updatedProducto == null) {
            return null; // Devuelve null si el producto no existe
        }
        return updatedProducto; // Devuelve el DTO del producto actualizado
    }
}

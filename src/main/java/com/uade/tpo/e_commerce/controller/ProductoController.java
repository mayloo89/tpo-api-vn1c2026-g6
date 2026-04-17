package com.uade.tpo.e_commerce.controller;

import java.util.List;

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

import com.uade.tpo.e_commerce.dto.ProductoRequestDTO;
import com.uade.tpo.e_commerce.dto.ProductoResponseDTO;
import com.uade.tpo.e_commerce.dto.ProductoUpdateDTO;
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
    public List<ProductoResponseDTO> getAllProductos() {
        return productoService.getAllProductos();
    }

    /**
     * Obtiene un producto específico por su ID.
     *
     * @param id identificador del producto
     * @return el producto encontrado
     */
    @GetMapping("/{id}") // Maneja solicitudes GET a la ruta /api/productos/{id}, donde {id} es un parámetro dinámico
    // Extrae el valor de {id} de la ruta y lo pasa como parámetro al método
    public ResponseEntity<ProductoResponseDTO> getProductoByID(@PathVariable Long id) {
        ProductoResponseDTO productoResponseDTO = productoService.getProductoById(id);
        return new ResponseEntity<ProductoResponseDTO>(productoResponseDTO, HttpStatus.OK); // Devuelve 200 OK con el DTO del producto
    }

    /**
     * Crea un nuevo producto en la base de datos.
     *
     * @param producto el producto a crear, recibido en el cuerpo de la solicitud como JSON
     * @return el producto creado con su ID asignado
     */
    @PostMapping // Maneja solicitudes POST a la ruta /api/productos
    // Lee el JSON del cuerpo de la solicitud y lo convierte en un objeto Producto
    public ResponseEntity<ProductoResponseDTO> saveProducto(@RequestBody ProductoRequestDTO productoRequestDTO) {
        ProductoResponseDTO savedProducto = productoService.saveProducto(productoRequestDTO);
        return new ResponseEntity<ProductoResponseDTO>(savedProducto, HttpStatus.CREATED); // Devuelve 201 Created con el DTO del producto creado
    }

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
     * @param id el ID del producto a actualizar
     * @param producto el DTO con los datos actualizados del producto, recibido en el cuerpo de la solicitud como JSON
     * @return el producto actualizado con status 200
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> updateProducto(@PathVariable Long id, @RequestBody ProductoUpdateDTO producto) {
        // Llama al servicio para actualizar el producto con el ID especificado y los datos proporcionados en el cuerpo de la solicitud
        ProductoResponseDTO updatedProducto = productoService.updateProducto(id, producto);
        return new ResponseEntity<ProductoResponseDTO>(updatedProducto, HttpStatus.OK); // Devuelve 200 OK con el DTO del producto actualizado
    }

}

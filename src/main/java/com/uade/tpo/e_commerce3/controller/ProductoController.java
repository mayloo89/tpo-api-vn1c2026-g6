package com.uade.tpo.e_commerce3.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.e_commerce3.dto.ProductoRequest;
import com.uade.tpo.e_commerce3.dto.ProductoResponse;
import com.uade.tpo.e_commerce3.dto.ProductoUpdateDTO;
import com.uade.tpo.e_commerce3.service.ProductoService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
// para acceder a este controlador, la URL base será /api/productos
@RequestMapping("/api/productos")
public class ProductoController {

    
    @Autowired
    private ProductoService productoService;

    //http://localhost:8080/api/productos -> devuelve la lista de productos
    @GetMapping
    public List<ProductoResponse> getAllProductos() {
        return productoService.getAllProductos();
    }

    
    @GetMapping("/{id}")
    public ProductoResponse getProductoById(@PathVariable Long id) {
        ProductoResponse productoDTO = productoService.getProductoById(id);
        return productoDTO; 
    }


    // del http://localhost:8080/api/productos/1 -> elimina el producto con id 1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductoById(@PathVariable Long id) {
        productoService.deleteProductoById(id);
        return ResponseEntity.noContent().build();
    }

    
    @PostMapping
    public ProductoResponse saveProducto(@RequestBody ProductoRequest productoDTO) {
        ProductoResponse savedProducto = productoService.saveProducto(productoDTO);
        return savedProducto;

    }
    
    @PutMapping("/{id}")
    public ProductoResponse updateProducto(@PathVariable Long id, @RequestBody ProductoUpdateDTO productoDTO) {
        return productoService.updateProducto(id, productoDTO);
    }
    
    /*@PostMapping("/agregar")
    public ResponseEntity<ProductoResponse> agregar(@RequestBody ProductoRequest request) {
        //TODO: process POST request
        
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.saveProducto(request));
    }*/
    
    
}
    

package com.uade.TPO_grupo6.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.uade.TPO_grupo6.model.Producto;
import com.uade.TPO_grupo6.repository.ProductoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional

public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    /**
     * Devuelve la lista completa de productos.
     */
    public List<Producto> getAllProductos() {
        return productoRepository.findAll();
    }

    /**
     * Busca un producto por su id.
     */
    public Producto getProductoById(Long id) {
        if (id == null) {
            return null;
        }
       return productoRepository.findById(id).orElse(null);
    }

    /**
     * Crea un nuevo producto.
     */
    public Producto addProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    /**
     * Elimina un producto por su id.
     */
    public void deleteProducto(Long id) {
        if (id != null) {
            productoRepository.deleteById(id);
        }
    }
    
}

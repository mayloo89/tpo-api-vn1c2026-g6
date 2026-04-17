package com.uade.tpo.e_commerce3.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.e_commerce3.dto.ProductoRequest;
import com.uade.tpo.e_commerce3.dto.ProductoResponse;
import com.uade.tpo.e_commerce3.dto.ProductoUpdateDTO;
import com.uade.tpo.e_commerce3.exception.PrecioNegativoException;
import com.uade.tpo.e_commerce3.exception.ProductoNotFoundException;
import com.uade.tpo.e_commerce3.model.Producto;
import com.uade.tpo.e_commerce3.repository.ProductoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProductoService {
 
    @Autowired
    private ProductoRepository productoRepository;
    
    public List<ProductoResponse> getAllProductos() {
        return productoRepository.findAll().stream()
                .map(producto -> new ProductoResponse(
                        producto.getId(),
                        producto.getNombre(),
                        producto.getDescripcion(),
                        producto.getPrecio(),
                        producto.getStock()))
                .collect(Collectors.toList());
    }

    public ProductoResponse getProductoById(Long id) {
        Producto producto = productoRepository.findById(id).orElse(null);
        if (producto == null) {
            throw new ProductoNotFoundException("Producto no encontrado con id: " + id );
        }
        ProductoResponse productoResponse = new ProductoResponse(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getStock());
        return productoResponse;
    }

    public void deleteProductoById(Long id) {
        Producto producto = productoRepository.findById(id).orElse(null);
        if (producto == null) {
            throw new ProductoNotFoundException("Producto no encontrado con id: " + id);
        }
        productoRepository.deleteById(id);
    }

    public ProductoResponse saveProducto(ProductoRequest productoDTO) {

        if (productoDTO.getPrecio() < 0) {
            throw new PrecioNegativoException();
        }
        if (productoDTO.getStock() == null || productoDTO.getStock() < 0) {
            throw new IllegalArgumentException("El stock no puede ser nulo ni negativo");
        }

        Producto producto = Producto.builder()
                .nombre(productoDTO.getNombre())
                .descripcion(productoDTO.getDescripcion())
                .precio(productoDTO.getPrecio())
                .stock(productoDTO.getStock())
                .build();
        
        Producto productoAdd= productoRepository.save(producto);
        ProductoResponse productoResponseAdd = new ProductoResponse(
                productoAdd.getId(),
                productoAdd.getNombre(),
                productoAdd.getDescripcion(),
                productoAdd.getPrecio(),
                productoAdd.getStock());
        return productoResponseAdd;
    }


    public ProductoResponse updateProducto(Long id, ProductoUpdateDTO productoDTO) {
    Producto producto = productoRepository.findById(id)
            .orElseThrow(() -> new ProductoNotFoundException("Producto no encontrado con id: " + id));

    if (productoDTO.getPrecio() < 0) {
        throw new PrecioNegativoException();
    }

    if (productoDTO.getStock() == null || productoDTO.getStock() < 0) {
        throw new IllegalArgumentException("El stock no puede ser nulo ni negativo");
    }

    producto.setPrecio(productoDTO.getPrecio());
    producto.setStock(productoDTO.getStock());

    Producto productoActualizado = productoRepository.save(producto);

    return new ProductoResponse(
            productoActualizado.getId(),
            productoActualizado.getNombre(),
            productoActualizado.getDescripcion(),
            productoActualizado.getPrecio(),
            productoActualizado.getStock());
}

}

package com.uade.tpo.e_commerce.service;

import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.uade.tpo.e_commerce.dto.ProductoDTO;
import com.uade.tpo.e_commerce.model.Producto;
import com.uade.tpo.e_commerce.repository.ProductoRepository;

/*
* En service se definen las clases que contienen la lógica de negocio para cada entidad.
* Estas clases interactúan con los repositories para realizar operaciones sobre los datos.
*/

@Service // Marca esta clase como un servicio de Spring, lo que permite que sea detectada y gestionada por el contenedor de Spring
@Transactional // Indica que los métodos de esta clase deben ejecutarse dentro de una transacción, garantiza la integridad de los datos
public class ProductoService {

    @Autowired // Inyecta automáticamente una instancia de ProductoRepository en esta clase
    private ProductoRepository productoRepository; // Repositorio para interactuar con la base de datos

    /**
     * Obtiene todos los productos almacenados en la base de datos.
     * Utiliza el servicio JPA {@link ProductoRepository#findAll()}
     *
     * @return lista de todos los productos
     */
    public List<Producto> getAllProductos() {
        return productoRepository.findAll();
    }

    public Producto getProductoById1(Long id) {
        return productoRepository.findById(id).orElse(null);
    }
    
    /**
     * Obtiene un producto por su identificador único.
     * Utiliza el servicio JPA {@link ProductoRepository#findById(Long)}
     *
     * @param id el identificador del producto
     * @return el producto encontrado, o null si no existe
     */
    public ProductoDTO getProductoById(Long id) {
        Producto producto = productoRepository.findById(id).orElse(null);
        if (producto == null) {
            return null; // Devuelve null si el producto no existe
        }
        ProductoDTO productoDTO = new ProductoDTO(producto.getId(), producto.getNombre(), producto.getDescripcion(), producto.getPrecio());
        return productoDTO; // Devuelve un DTO con los datos del producto encontrado
    }

    /**
     * Guarda un nuevo producto o actualiza uno existente en la base de datos.
     * Utiliza el servicio JPA {@link ProductoRepository#save(Object)}
     *
     * @param productoDTO el producto a guardar o actualizar
     * @return el producto guardado con su ID asignado
     */
    public ProductoDTO saveProducto(ProductoDTO productoDTO) { 
        if (productoDTO.getPrecio() < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }

        Producto producto = Producto.builder()
                .nombre(productoDTO.getNombre())
                .descripcion(productoDTO.getDescripcion())
                .precio(productoDTO.getPrecio())
                .build();
        Producto productoAdd= productoRepository.save(producto);
        ProductoDTO productoDTOAdd= new ProductoDTO(productoAdd.getId(), productoAdd.getNombre(), productoAdd.getDescripcion(), productoAdd.getPrecio());
        return productoDTOAdd;
    }

    /**
     * Elimina un producto por su id.
     */
    public void deleteProductoById(Long id) {
        productoRepository.deleteById(id);
    }
    

    public Producto updateProducto(Long id, ProductoDTO productoDTO) {
        Producto existingProducto = getProductoById1(id);
        if (existingProducto == null) {
            return null; // Devuelve null si el producto no existe
        }
        existingProducto.setNombre(productoDTO.getNombre());
        existingProducto.setDescripcion(productoDTO.getDescripcion());
        existingProducto.setPrecio(productoDTO.getPrecio());

        return productoRepository.save(existingProducto);
    }

    
    public ProductoDTO updateProducto1(@PathVariable Long id, @RequestBody ProductoDTO productoDTO) {
    Producto updatedProducto = updateProducto(id, productoDTO);

    if (updatedProducto == null) {
        return null;
    }

    return new ProductoDTO(
        updatedProducto.getId(),
        updatedProducto.getNombre(),
        updatedProducto.getDescripcion(),
        updatedProducto.getPrecio()
    );
}
}

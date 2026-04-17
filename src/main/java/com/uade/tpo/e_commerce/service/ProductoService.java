package com.uade.tpo.e_commerce.service;

import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.e_commerce.dto.ProductoResponseDTO;
import com.uade.tpo.e_commerce.dto.ProductoRequestDTO;
import com.uade.tpo.e_commerce.dto.ProductoUpdateDTO;
import com.uade.tpo.e_commerce.exception.ProductoNotFoundException;
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
    public List<ProductoResponseDTO> getAllProductos() {
        return productoRepository.findAll().stream()
                .map(this::toProductoResponseDTO)
                .toList();
    }
    
    /**
     * Obtiene un producto por su identificador único.
     * Utiliza el servicio JPA {@link ProductoRepository#findById(Long)}
     *
     * @param id el identificador del producto
     * @return el producto encontrado
     */
    public ProductoResponseDTO getProductoById(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException("Producto no encontrado"));
        return toProductoResponseDTO(producto); // Devuelve un DTO con los datos del producto encontrado
    }

    /**
     * Guarda un nuevo producto o actualiza uno existente en la base de datos.
     * Utiliza el servicio JPA {@link ProductoRepository#save(Object)}
     *
     * @param productoRequestDTO el DTO con los datos del producto a guardar o actualizar
     * @return el producto guardado o actualizado, con su ID asignado si es nuevo
     */
    public ProductoResponseDTO saveProducto(ProductoRequestDTO productoRequestDTO) { 
        if (productoRequestDTO.getPrecio() < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }

        Producto producto = Producto.builder()
                .nombre(productoRequestDTO.getNombre())
                .descripcion(productoRequestDTO.getDescripcion())
                .precio(productoRequestDTO.getPrecio())
            .stock(productoRequestDTO.getStock())
                .build();
        Producto productoAdd= productoRepository.save(producto);
        return toProductoResponseDTO(productoAdd);
    }

    /**
     * Elimina un producto por su id.
     */
    public void deleteProductoById(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new ProductoNotFoundException("Producto no existe");
        }
        productoRepository.deleteById(id);
    }
    

    public ProductoResponseDTO updateProducto(Long id, ProductoUpdateDTO productoRequestDTO) {
        Producto existingProducto = productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException("Producto no encontrado"));

        // Actualizamos solo los campos que se permiten modificar (precio y stock)
        existingProducto.setPrecio(productoRequestDTO.getPrecio());
        existingProducto.setStock(productoRequestDTO.getStock());

        Producto productoUpdated = productoRepository.save(existingProducto);
        return toProductoResponseDTO(productoUpdated);
    }

    private ProductoResponseDTO toProductoResponseDTO(Producto producto) {
        return new ProductoResponseDTO(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getStock());
    }
}

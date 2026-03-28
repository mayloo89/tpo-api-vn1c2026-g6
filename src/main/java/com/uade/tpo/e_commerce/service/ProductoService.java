package com.uade.tpo.e_commerce.service;

import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    
    /**
     * Obtiene un producto por su identificador único.
     * Utiliza el servicio JPA {@link ProductoRepository#findById(Long)}
     *
     * @param id el identificador del producto
     * @return el producto encontrado, o null si no existe
     */
    public Producto getProductoById(Long id) {
        return productoRepository.findById(id).orElse(null);
    }

    /**
     * Guarda un nuevo producto o actualiza uno existente en la base de datos.
     * Utiliza el servicio JPA {@link ProductoRepository#save(Object)}
     *
     * @param producto el producto a guardar o actualizar
     * @return el producto guardado con su ID asignado
     */
    public Producto saveProducto(Producto producto) { 
        return productoRepository.save(producto); 
        
    }

    /**
     * Elimina un producto por su id.
     */
    public void deleteProducto(Long id) {
        if (id != null && productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
        }
    }
    
}

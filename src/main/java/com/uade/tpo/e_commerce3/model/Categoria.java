package com.uade.tpo.e_commerce3.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import java.util.List;
import java.util.ArrayList;

@Data
@Entity
@Builder
@Table(name = "categorias")
public class Categoria {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)    
    private String nombre;

    @Builder.Default
    @ManyToMany(mappedBy = "categorias")
    private List<Producto> productos = new ArrayList<>();
    //categoria.getProductos(); // trae los productos de la categoria
}

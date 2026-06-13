package com.uade.tpo.e_commerce.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.uade.tpo.e_commerce.dto.ProductoResponseDTO;
import com.uade.tpo.e_commerce.exception.ProductoNotFoundException;
import com.uade.tpo.e_commerce.model.Producto;
import com.uade.tpo.e_commerce.repository.ProductoRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ImagenController {

    private final ProductoRepository productoRepository;

    @Value("${app.upload.dir:./uploads}")
    private String uploadDir;

    @PostMapping("/{id}/imagen")
    public ResponseEntity<ProductoResponseDTO> uploadImagen(
            @PathVariable Long id,
            @RequestParam("archivo") MultipartFile archivo) throws IOException {

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException("Producto no encontrado con id: " + id));

        String extension = obtenerExtension(archivo.getOriginalFilename());
        String nombreArchivo = UUID.randomUUID().toString() + extension;

        Path dirPath = Paths.get(uploadDir);
        Files.createDirectories(dirPath);
        Files.copy(archivo.getInputStream(), dirPath.resolve(nombreArchivo), StandardCopyOption.REPLACE_EXISTING);

        producto.setImagenUrl("/uploads/" + nombreArchivo);
        productoRepository.save(producto);

        ProductoResponseDTO response = ProductoResponseDTO.builder()
                .id(producto.getId())
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .precio(producto.getPrecio())
                .stock(producto.getStock())
                .imagen(producto.getImagenUrl())
                .build();

        return ResponseEntity.ok(response);
    }

    private String obtenerExtension(String nombreOriginal) {
        if (nombreOriginal == null || !nombreOriginal.contains(".")) {
            return "";
        }
        return nombreOriginal.substring(nombreOriginal.lastIndexOf("."));
    }
}

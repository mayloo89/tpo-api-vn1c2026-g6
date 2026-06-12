package com.uade.tpo.e_commerce.service;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.uade.tpo.e_commerce.dto.ProductoResponseDTO;
import com.uade.tpo.e_commerce.exception.ProductoNotFoundException;
import com.uade.tpo.e_commerce.exception.UsuarioNotFoundException;
import com.uade.tpo.e_commerce.model.Producto;
import com.uade.tpo.e_commerce.model.Usuario;
import com.uade.tpo.e_commerce.repository.ProductoRepository;
import com.uade.tpo.e_commerce.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class FavoritoService {

    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;

    public FavoritoService(UsuarioRepository usuarioRepository, ProductoRepository productoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
    }

    public List<ProductoResponseDTO> getFavoritos(Authentication authentication) {
        Usuario usuario = getUsuarioActual(authentication);
        return usuario.getFavoritos().stream().map(this::toResponse).toList();
    }

    public List<ProductoResponseDTO> addFavorito(Authentication authentication, Long productoId) {
        Usuario usuario = getUsuarioActual(authentication);
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new ProductoNotFoundException("Producto no encontrado con id: " + productoId));

        boolean yaExiste = usuario.getFavoritos().stream()
                .anyMatch(favorito -> favorito.getId().equals(producto.getId()));

        if (!yaExiste) {
            usuario.getFavoritos().add(producto);
            usuarioRepository.save(usuario);
        }

        return getFavoritos(authentication);
    }

    public List<ProductoResponseDTO> removeFavorito(Authentication authentication, Long productoId) {
        Usuario usuario = getUsuarioActual(authentication);
        usuario.getFavoritos().removeIf(producto -> producto.getId().equals(productoId));
        usuarioRepository.save(usuario);
        return getFavoritos(authentication);
    }

    private Usuario getUsuarioActual(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            throw new UsuarioNotFoundException("Usuario no autenticado");
        }

        return usuarioRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new UsuarioNotFoundException(
                        "Usuario no encontrado con email: " + authentication.getName()));
    }

    private ProductoResponseDTO toResponse(Producto producto) {
        return new ProductoResponseDTO(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getStock());
    }
}
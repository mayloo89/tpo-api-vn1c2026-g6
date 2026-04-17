package com.uade.tpo.e_commerce.service;

import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.e_commerce.exception.UsuarioNotFoundException;
import com.uade.tpo.e_commerce.model.Usuario;
import com.uade.tpo.e_commerce.repository.UsuarioRepository;

@Service
@Transactional

public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Devuelve la lista completa de usuarios registrados.
     */
    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    /**
     * Busca un usuario por su id.
     */
    public Usuario getUsuarioById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado"));
    }

    /**
     * Crea un nuevo usuario o actualiza uno existente si el id ya existe.
     */
    public Usuario addUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    /**
     * Actualiza los datos de un usuario existente.
     */
    public Usuario updateUsuario(Long id, Usuario usuario) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        Usuario existing = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado"));

        existing.setNombre(usuario.getNombre());
        existing.setEmail(usuario.getEmail());
        existing.setPassword(usuario.getPassword());
        return usuarioRepository.save(existing);
    }

    /**
     * Elimina un usuario por su id si existe en la base de datos.
     */
    public void deleteUsuario(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }

        if (!usuarioRepository.existsById(id)) {
            throw new UsuarioNotFoundException("Usuario no existe");
        }

        usuarioRepository.deleteById(id);
    }

}

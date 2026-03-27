package com.uade.TPO_grupo6.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.TPO_grupo6.model.Usuario;
import com.uade.TPO_grupo6.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")

public class UsuarioController {
    
    @Autowired
    private UsuarioService usuarioService;

    /**
     * Endpoint para obtener todos los usuarios.
     * GET /api/usuarios
     */
    @GetMapping
    public List<Usuario> getAllUsuarios() {
        return usuarioService.getAllUsuarios();
    }

    /**
     * Endpoint para obtener un usuario por su id.
     * GET /api/usuarios/{id}
     */
    @GetMapping("/{id}")
    public Usuario getUsuarioById(@PathVariable Long id) {
        return usuarioService.getUsuarioById(id);
    }

    /**
     * Endpoint para crear un nuevo usuario.
     * POST /api/usuarios
     */
    @PostMapping
    public Usuario addUsuario(@RequestBody Usuario usuario) {
        return usuarioService.addUsuario(usuario);
    }

    /**
     * Endpoint para actualizar un usuario existente.
     * PUT /api/usuarios/{id}
     */
    @PutMapping("/{id}")
    public Usuario updateUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        return usuarioService.updateUsuario(id, usuario);
    }

    /**
     * Endpoint para eliminar un usuario por su id.
     * DELETE /api/usuarios/{id}
     */
    @DeleteMapping("/{id}")
    public void deleteUsuario(@PathVariable Long id) {
        usuarioService.deleteUsuario(id);
    }
}

package com.uade.tpo.e_commerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.uade.tpo.e_commerce.dto.UsuarioRequestDTO;
import com.uade.tpo.e_commerce.dto.UsuarioResponseDTO;
import com.uade.tpo.e_commerce.exception.UsuarioNotFoundException;
import com.uade.tpo.e_commerce.model.Role;
import com.uade.tpo.e_commerce.model.Usuario;
import com.uade.tpo.e_commerce.repository.UsuarioRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<UsuarioResponseDTO> getAllUsuarios() {
        return usuarioRepository.findAll().stream()
            .map(this::toResponse)
                .toList();
                }

    public UsuarioResponseDTO getUsuarioById(Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario == null) {
            throw new UsuarioNotFoundException ("Usuario no encontrado con id: " + id);
        }
        return toResponse(usuario);
    }

    public UsuarioResponseDTO addUsuario(UsuarioRequestDTO usuarioRequest) {
        Usuario usuario = Usuario.builder()
                .nombre(usuarioRequest.getNombre())
                .apellido(usuarioRequest.getApellido())
                .email(usuarioRequest.getEmail())
                .password(passwordEncoder.encode(usuarioRequest.getPassword()))
                .role(usuarioRequest.getRole() != null ? Role.valueOf(usuarioRequest.getRole()) : Role.USER)
                .build();
        
        Usuario saved = usuarioRepository.save(usuario);
        return toResponse(saved);
    }

    public UsuarioResponseDTO updateUsuario(Long id, UsuarioRequestDTO usuarioRequest) {
        Usuario existing = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado con id: " + id));

        existing.setNombre(usuarioRequest.getNombre());
        existing.setApellido(usuarioRequest.getApellido());
        existing.setEmail(usuarioRequest.getEmail());
        if (usuarioRequest.getRole() != null && !usuarioRequest.getRole().isBlank()) {
            existing.setRole(Role.valueOf(usuarioRequest.getRole()));
        }
        if (usuarioRequest.getPassword() != null && !usuarioRequest.getPassword().isBlank()) {
            existing.setPassword(passwordEncoder.encode(usuarioRequest.getPassword()));
        }

        Usuario updated = usuarioRepository.save(existing);
        return toResponse(updated);
    }

    public void deleteUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new UsuarioNotFoundException("Usuario no encontrado con id: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    private UsuarioResponseDTO toResponse(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getRole() != null ? usuario.getRole().name() : Role.USER.name());
    }

}

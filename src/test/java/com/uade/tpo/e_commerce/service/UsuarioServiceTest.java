package com.uade.tpo.e_commerce.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.uade.tpo.e_commerce.dto.UsuarioRequestDTO;
import com.uade.tpo.e_commerce.exception.UsuarioNotFoundException;
import com.uade.tpo.e_commerce.model.Role;
import com.uade.tpo.e_commerce.model.Usuario;
import com.uade.tpo.e_commerce.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void addUsuario_encriptaPassword() {
        UsuarioRequestDTO request = new UsuarioRequestDTO("Nombre", "Apellido", "mail@test.com", "secret", "USER");
        when(passwordEncoder.encode("secret")).thenReturn("encoded");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var response = usuarioService.addUsuario(request);

        assertThat(response.getEmail()).isEqualTo("mail@test.com");
        assertThat(response.getRole()).isEqualTo(Role.USER.name());
    }

    @Test
    void updateUsuario_lanzaSiNoExiste() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());
        UsuarioRequestDTO request = new UsuarioRequestDTO("Nombre", "Apellido", "mail@test.com", null, null);

        assertThatThrownBy(() -> usuarioService.updateUsuario(99L, request))
                .isInstanceOf(UsuarioNotFoundException.class);
    }
}

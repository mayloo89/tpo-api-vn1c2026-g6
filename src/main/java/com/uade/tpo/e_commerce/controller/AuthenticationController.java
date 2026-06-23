package com.uade.tpo.e_commerce.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.e_commerce.dto.AuthenticationResponseDTO;
import com.uade.tpo.e_commerce.dto.LoginRequestDTO;
import com.uade.tpo.e_commerce.dto.RegisterRequestDTO;
import com.uade.tpo.e_commerce.model.Usuario;
import com.uade.tpo.e_commerce.repository.UsuarioRepository;
import com.uade.tpo.e_commerce.service.AuthenticationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UsuarioRepository usuarioRepository;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequestDTO request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDTO> login(@RequestBody LoginRequestDTO request, HttpServletResponse response) {
        AuthenticationResponseDTO authResponse = authenticationService.authenticate(request);

        String token = authResponse.getToken();
        String cookie = String.format("token=%s; HttpOnly; SameSite=Lax; Path=/; Max-Age=86400", token);
        response.addHeader("Set-Cookie", cookie);

        return ResponseEntity.ok(AuthenticationResponseDTO.builder()
                .mensaje(authResponse.getMensaje())
                .nombre(authResponse.getNombre())
                .email(request.getEmail())
                .build());
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        String cookie = "token=; HttpOnly; SameSite=Lax; Path=/; Max-Age=0";
        response.addHeader("Set-Cookie", cookie);
        return ResponseEntity.ok("Logout successful");
    }

    @GetMapping("/me")
    public ResponseEntity<AuthenticationResponseDTO> me(@AuthenticationPrincipal String email) {
        if (email == null) {
            return ResponseEntity.status(401).build();
        }
        Usuario user = usuarioRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(AuthenticationResponseDTO.builder()
                .mensaje("Usuario autenticado")
                .nombre(user.getNombre())
                .email(user.getEmail())
                .build());
    }
}

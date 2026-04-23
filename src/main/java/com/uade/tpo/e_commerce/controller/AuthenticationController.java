package com.uade.tpo.e_commerce.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.e_commerce.dto.AuthenticationResponseDTO;
import com.uade.tpo.e_commerce.dto.LoginRequestDTO;
import com.uade.tpo.e_commerce.dto.RegisterRequestDTO;
import com.uade.tpo.e_commerce.service.AuthenticationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
//anotación de Lombok que genera automáticamente un constructor que incluye todos los campos marcados como final, es igual que usar @autowired 
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    //http://localhost:8080/api/auth/register con metodo post http, enviar un body -> crear un usuario
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequestDTO request) {
        //request tiene los datos del usuario a registrar, como nombre, email y contraseña
        return ResponseEntity.ok(authenticationService.register(request));
    }

    //http://localhost:8080/api/auth/login con metodo post http, enviar un body -> loguear un usuario
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDTO> login(@RequestBody LoginRequestDTO request) {
        AuthenticationResponseDTO response = authenticationService.authenticate(request);
        return ResponseEntity.ok(response);
    }
}

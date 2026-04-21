package com.uade.tpo.e_commerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
// DTO para la respuesta de autenticación, que incluye el token JWT, un mensaje y el nombre del usuario.
public class AuthenticationResponseDTO {
    private String mensaje;
    private String token;
}

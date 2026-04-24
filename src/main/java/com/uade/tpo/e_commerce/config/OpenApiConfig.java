package com.uade.tpo.e_commerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("TPO API - e-commerce")
                        .version("1.0.0")
                        .description("API REST para el trabajo practico grupal de Aplicaciones Interactivas."));
    }
}

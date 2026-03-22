package com.uade.tpo.e_commerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la aplicación e-commerce.
 * Punto de entrada de la aplicación Spring Boot.
 * Activa la configuración automática de Spring Boot y escanea el paquete actual y sus subpaquetes
 * para detectar componentes como @Service, @Controller, @Repository, etc.
 */
@SpringBootApplication // Habilita la configuración automática de Spring Boot, escaneo de componentes y configuración de aplicación
public class ECommerceApplication {

	/**
	 * Método principal que inicia la aplicación Spring Boot.
	 *
	 * @param args argumentos de línea de comandos (opcional)
	 */
	public static void main(String[] args) {
		SpringApplication.run(ECommerceApplication.class, args);
	}

}

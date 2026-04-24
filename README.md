# TPO Backend - E-commerce API

Trabajo practico grupal de Aplicaciones Interactivas (UADE) - Grupo 6. 
Cursada: Primer cuatrimestre 2026, viernes turno noche. 

Proyecto backend con Spring Boot y API REST para un marketplace basico.

## Tecnologias

- Java 17
- Spring Boot, Spring Data JPA, Spring Security
- MySQL
- JWT
- Lombok
- Docker Compose
- Swagger/OpenAPI (Springdoc)

## Levantar con Docker

Requisitos: Docker instalado.

1) Levantar backend y base de datos:

```bash
docker compose up --build
```

2) La API queda en:

```
http://localhost:8080
```

La base MySQL queda en `localhost:3306` (usuario `root`, password `root`).

## Swagger / OpenAPI

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Autenticacion

La API usa JWT. Para acceder a endpoints protegidos:

1) POST `/api/auth/register`
2) POST `/api/auth/login` (devuelve token)
3) Usar header: `Authorization: Bearer <token>`

## Variables de entorno

Opcional en `.env` (en la raiz del proyecto):

```
JWT_SECRET=tu_clave_segura
```

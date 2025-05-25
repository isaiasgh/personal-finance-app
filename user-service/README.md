# User Service

`user-service` is a Spring Boot microservice responsible for user registration, authentication, and basic user data management. It provides JWT-based security and integrates with Eureka for service discovery.

## Endpoints Overview

| Method | Endpoint               | Description                        |
|--------|------------------------|------------------------------------|
| POST   | /auth/register         | Register a new user                |
| POST   | /auth/login            | Authenticate user and get JWT      |
| GET    | /users/all             | Get a list of all users            |
| GET    | /users                 | Get basic info of authenticated user |
| PUT    | /users                 | Update user password               |
| GET    | /users/{username}      | Check if a username is available   |

> **Note:** All endpoints are accessible via the `/user-service` base path.

## Security

- JWT-based authentication
- Custom components:
  - `JwtProvider`
  - `JwtAuthenticationFilter`
  - `JwtAuthenticationEntryPoint`
  - `CustomUserDetailsService`
- Configured in `SecurityConfig.java`

## Database

### Entities

- **User**: stores basic user information (email, username, etc.).
- **PasswordLog**: stores password history and failed login attempts for auditing.

Uses **PostgreSQL** via Spring Data JPA.

## Configuration Summary

This service uses the following key configuration properties:

- `spring.application.name`: `user-service`
- `server.port`: `${SERVER_PORT}`
- `spring.datasource.url`: PostgreSQL connection
- `eureka.client.service-url.defaultZone`: Eureka registry URL
- `security.constant`: Custom security constant used for JWT signing
- `expiration.time.token`: JWT token expiration in milliseconds

> Full configuration can be found in [`application.yaml`](src/main/resources/application.yaml).

## Error Handling

Includes a centralized global exception handler and custom exceptions for different error scenarios (e.g., invalid login, user not found, duplicate username/email).

## Testing

* Includes basic unit and integration tests.
* Security-related components are partially tested.

## Dependencies

Main libraries used:

* Spring Boot Starter Web
* Spring Security
* Spring Data JPA
* PostgreSQL Driver
* Eureka Client
* JJWT (JSON Web Token)
* MapStruct
* Lombok

> 📌 **Note**: This service is intended to be run as part of a microservice system. Docker and deployment instructions will be added in future iterations.
# Category Service

`category-service` is a Spring Boot microservice responsible for managing personal finance categories. It allows authenticated users to create, retrieve, update, and delete their own categories. It integrates with Eureka for service discovery and communicates with `user-service` through a Feign client to validate users.

## Endpoints Overview

| Method | Endpoint                 | Description                            |
|--------|--------------------------|----------------------------------------|
| POST   | /categories              | Create a new category for the user     |
| GET    | /categories              | Get all categories of the user         |
| GET    | /categories/{id}         | Get a specific category by ID          |
| GET    | /categories/search       | Search categories by name              |
| PUT    | /categories/{id}         | Update an existing category            |
| DELETE | /categories/{id}         | Delete a category                      |

>📌 **Note**: All endpoints are accessible via the `/category-service` base path.  
🔐 All endpoints require a valid JWT in the `Authorization` header.

## Security

Although this service **does not use Spring Security**, it enforces **JWT-based authentication manually** by extracting and validating the token in service methods.  
It uses a **Feign client** to contact the `user-service` and verify if a username exists and is valid.

Custom security handling includes:
- `FeignClientInterceptor` for forwarding JWT in Feign calls
- Manual JWT parsing in the service layer
- Token is expected in the `Authorization` header as: `Bearer <token>`


## Database

### Entities

- **Category**: stores the name and ownership (username) of a category.

### Data Initialization

On startup, a list of base categories is automatically created for each new user if not already present:

```java
List<String> baseCategories = List.of("Health", "Entertainment", "Transport", "Food");
````

### Technology

Uses PostgreSQL via Spring Data JPA.

## Configuration Summary

This service uses the following key configuration properties:

* `spring.application.name`: `category-service`
* `server.port`: port number where the service runs (e.g., `8082`)
* `server.servlet.context-path`: base path for all endpoints (`/category-service`)
* `spring.datasource.url`: PostgreSQL connection URL (uses environment variables)
* `spring.jpa.*`: Hibernate and JPA settings
* `eureka.client.service-url.defaultZone`: Eureka service registry URL
* `security.constant`: custom JWT secret used for token validation (shared with `user-service`)

> Full configuration can be found in [`application.yaml`](src/main/resources/application.yaml).

All sensitive values (database credentials, JWT secret, etc.) are externalized using environment variables.

## Feign Client Integration

The service uses a Feign client to communicate with `user-service`:

```java
@FeignClient("user-service")
public interface UserAuthClient {
    @GetMapping("/user-service/users/{username}")
    boolean isUsernameValid(@PathVariable String username);
}
```

## Error Handling

Includes custom exceptions for invalid categories, unauthorized access, or missing resources. Exceptions are handled globally and translated into meaningful HTTP responses.

## Testing

Includes basic unit and integration tests for:

* Category creation and retrieval
* User-category validation logic
* Feign communication with `user-service`

## Dependencies

Main libraries used:

* Spring Boot Starter Web
* Spring Data JPA
* Spring Cloud OpenFeign
* PostgreSQL Driver
* Eureka Client
* Lombok
* MapStruct
* Jakarta Validation

>📌 **Note**: This service is part of a larger microservice architecture. Docker and deployment instructions will be added in future iterations.
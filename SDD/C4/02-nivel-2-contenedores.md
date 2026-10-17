# C4 Nivel 2 — Contenedores

## Propósito

Describir la arquitectura de despliegue y ejecución de **nexMarket**, ilustrando los límites de cada contenedor, las responsabilidades de procesamiento y almacenamiento, y los protocolos de comunicación utilizados en la red.

## Diagrama de Contenedores

```mermaid
C4Container
    title Contenedores — Plataforma nexMarket

    Person(usuario, "Usuario del Marketplace", "Navegador web con token JWT en memoria")

    System_Boundary(boundary, "Plataforma nexMarket (docker-compose)") {
        Container(spa, "Frontend SPA", "React / Vite (:5173)", "Aplicación cliente; gestiona el estado visual y envía el encabezado Authorization: Bearer <jwt>")
        Container(app, "nexmarket-app", "Spring Boot 4.x + Java 17 (:8080)", "Arquitectura Hexagonal: rest → useCases → domain → persistence/security. Expone API REST en SERVER_PORT")
        ContainerDb(mysql, "mysql-db", "MySQL 8.0 (:3306)", "Base de datos relacional para entidades transaccionales (users, buyers, sellers, products, orders, inventories, etc.)")
        ContainerDb(mongo, "mongo-db", "MongoDB 6.0 (:27017)", "Base de datos documental NoSQL para la colección audit_logs inmutable")
    }

    Rel(usuario, spa, "Interactúa con la interfaz gráfica")
    Rel(spa, app, "JSON/HTTPS :8080 con Authorization: Bearer <JWT> y X-Request-Id")
    Rel(app, mysql, "JDBC (MySQL Connector/J) según DB_URL, DB_USERNAME, DB_PASSWORD")
    Rel(app, mongo, "Protocolo Mongo según MONGODB_URI (spring.mongodb.uri)")
```

## Especificación de Contenedores y Variables de Entorno

| Contenedor | Imagen / Runtime | Variables de Entorno Clave | Responsabilidad y Flujo |
|---|---|---|---|
| `spa` | React / Vite (`:5173`) | `VITE_API_URL` | Interfaz de usuario; inicia los flujos de login y peticiones REST autenticadas |
| `nexmarket-app` | `nexMarket/Dockerfile` (Maven 3.9 + Temurin 17) | `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `MONGODB_URI`, `JWT_SECRET`, `JWT_EXPIRATION_MS`, `FRONTEND_ORIGIN`, `SERVER_PORT` | Recibe peticiones HTTP, autentica mediante `JwtAuthenticationFilter`, autoriza por rol en `SecurityConfig`, orquesta casos de uso y servicios de dominio |
| `mysql-db` | `mysql:8.0` | `MYSQL_DATABASE=nexmarket_db`, `MYSQL_ROOT_PASSWORD`, `MYSQL_USER`, `MYSQL_PASSWORD` | Almacenamiento ACID de usuarios, productos, bodegas, carritos, órdenes y transacciones de negocio |
| `mongo-db` | `mongo:6.0` | Puerto `27017`, base de datos `nexmarket_audit_db` | Registro de trazabilidad de operaciones críticas en la colección `audit_logs` |

## Ciclo de Red de una Petición

```mermaid
sequenceDiagram
    participant SPA as Frontend SPA (:5173)
    participant APP as nexmarket-app (:8080)
    participant MY as MySQL (:3306)
    participant MO as MongoDB (:27017)

    SPA->>APP: HTTP (GET/POST/PUT/DELETE) /api/v1/** con Bearer Token (salvo /public/**)
    APP->>APP: JwtAuthenticationFilter intercepta, valida firma y reconstruye User desde claims
    APP->>APP: SecurityConfig verifica que el rol coincida con el prefijo autorizado
    APP->>MY: JPA/Hibernate consulta o actualiza el modelo relacional
    opt Operación auditable
        APP->>MO: AuditLogMongoAdapter inserta documento en audit_logs
    end
    APP-->>SPA: HTTP 200/201/204 + DTO de respuesta (o ErrorResponse con código HTTP)
```

### Convenciones Semánticas HTTP
* `200 OK` / `201 CREATED` / `204 NO CONTENT`: Operación completada con éxito.
* `400 BAD REQUEST`: Error de validación en los datos de entrada (`@Valid`).
* `401 UNAUTHORIZED`: Credenciales inválidas o ausencia de token en ruta protegida.
* `403 FORBIDDEN`: Token expirado, versión de token obsoleta o rol insuficiente para el prefijo de la ruta.
* `404 NOT FOUND`: Recurso no encontrado (manejado por `GlobalExceptionHandler` con envelope `ErrorResponse`).

# C4 Nivel 3 — Componentes

## Propósito

Descomponer el contenedor **`nexmarket-app`** para detallar los componentes que participan en el ciclo de vida de cada petición: controladores REST, adaptadores de casos de uso, servicios de dominio, puertos y adaptadores secundarios.

## Diagrama de Componentes

```mermaid
C4Component
    title Componentes — nexmarket-app (Arquitectura Hexagonal)

    Container_Boundary(app, "nexmarket-app") {
        Component(filter, "JwtAuthenticationFilter", "OncePerRequestFilter", "Intercepta el encabezado Bearer, valida el token y reconstruye el User")
        Component(secfg, "SecurityConfig", "Spring Security", "Reglas hasRole por prefijo /api/v1/**, CORS y PasswordEncoder BCrypt")
        Component(rest, "REST Controllers ×6", "Spring MVC RestController", "PublicAccess, Buyer, Seller, LogisticsOperator, Administrator, Supervisor")
        Component(rmap, "REST DTOs + Mappers", "Jakarta Validation & Mappers", "Validación @Valid de RequestDTOs y conversión bidireccional DTO ↔ Dominio")
        Component(uc, "UseCases ×6", "Spring Service", "Implementan domain/ports/in y orquestan los servicios de dominio")
        Component(pin, "Puertos de Entrada (in)", "Java Interfaces", "PublicAccessPort, BuyerPort, SellerPort, LogisticsOperatorPort, AdministratorPort, SupervisorPort")
        Component(svc, "Servicios de Dominio ×47", "Java Puro (13 subdominios)", "catalog, cart, order, inventory, logistics, invoicing, returns, user, seller, buyer, warehouse, operation, authorization")
        Component(pout, "Puertos de Salida (out)", "Java Interfaces", "20 puertos: UserRepositoryPort, BuyerRepositoryPort, JwtServicePort, PasswordServicePort, etc.")
        Component(jpa, "Adaptadores JPA", "Spring Data JPA", "Persistencia relacional contra MySQL (UserJpaAdapter, etc.)")
        Component(mongo, "AuditLogMongoAdapter", "Spring Data MongoDB", "Persistencia documental en colección audit_logs")
        Component(jwt, "JwtProvider + JwtClaimsMapper", "JJWT", "Emisión/validación de tokens y mapeo User ↔ claims")
        Component(seed, "DatabaseSeeder", "CommandLineRunner @Order(1)", "Semillas automáticas de todos los roles (Password123*)")
    }

    Rel(rest, filter, "Filtro previo en la cadena de seguridad")
    Rel(filter, jwt, "validateToken / reconstructUser")
    Rel(rest, rmap, "Mapeo RequestDTO → Modelo")
    Rel(rest, pin, "Invoca método del puerto in pasando el User autenticado")
    Rel(uc, pin, "Implementa el contrato del rol")
    Rel(uc, svc, "Orquesta reglas de negocio")
    Rel(svc, pout, "Consume puertos de salida")
    Rel(jpa, pout, "Implementa contrato hacia MySQL")
    Rel(mongo, pout, "Implementa contrato hacia MongoDB")
    Rel(jwt, pout, "Implementa JwtServicePort")
```

## Ciclo de Vida de una Petición Hexagonal

El siguiente diagrama de secuencia representa la interacción entre componentes para cualquier endpoint del marketplace:

```mermaid
sequenceDiagram
    participant F as JwtAuthenticationFilter
    participant C as Controller (por Rol)
    participant M as REST Mapper
    participant P as Puerto de Entrada (Port In)
    participant U as UseCaseImpl
    participant S as Servicio de Dominio Puro
    participant O as Puerto de Salida (Port Out)
    participant A as Adaptador de Salida (JPA / Mongo / JWT)

    F->>C: Inyecta AuthenticatedUserPrincipal con el User reconstruido
    C->>M: toDomain(RequestDTO)
    M-->>C: Modelo de dominio con datos validados
    C->>P: invoca operacion(authenticatedUser, modelo...)
    P->>U: Despacha llamada al UseCase bean
    U->>S: Invoca servicio de dominio puro correspondiente
    S->>O: Consulta o almacena mediante el puerto de salida
    O->>A: Adaptador ejecuta la interacción tecnológica (JPA/Mongo/Bcrypt)
    A-->>O: Retorna entidad o resultado
    O-->>S: Retorna modelo de dominio
    S-->>U: Retorna resultado procesado con reglas de negocio
    U-->>P: Retorna respuesta del caso de uso
    P-->>C: Retorna modelo al controlador
    C->>M: toResponseDTO(resultado)
    M-->>C: ResponseDTO estructurado
    C-->>F: ResponseEntity 200/201 + ResponseDTO
```

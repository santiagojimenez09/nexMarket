# Software Architecture

## Overview

The NexusMarket Marketplace Information Management System follows a **Hexagonal Architecture (Ports and Adapters)** combined with **Domain-Driven Design (DDD)** principles.

The primary objective of this architecture is to isolate the business domain — users, products, inventory, orders, logistics, returns and refunds — from external technologies, ensuring that business rules remain independent from frameworks, databases, communication protocols, and infrastructure concerns.

This approach promotes maintainability, scalability, testability, and technology independence, and is consistent with the technologies already declared in `pom.xml`: Spring Boot, Spring Data JPA (MySQL), Spring Data MongoDB, and Spring Security.

---

# Architectural Principles

The architecture is based on the following principles:

- Domain-first design.
- Separation of concerns.
- Dependency inversion.
- Technology independence.
- High cohesion.
- Low coupling.
- Explicit boundaries between layers.

The domain contains all business rules described in the Functional Specification and never depends on external technologies.

---

# Architecture Layers

The application is organized into four major components:

```text
Application
│
├── Adapters
│
├── Domain
│
└── Infrastructure
```

Each component has a clearly defined responsibility.

---

# Package Structure

The package structure below extends what already exists in the repository (`application/domains` and `application/valueObjects`) with the additional layers required by Hexagonal Architecture.

```text
src/
└── main/
    └── java/
        └── application/
            │
            ├── NexMarketApplication.java
            │
            ├── domains/
            │   ├── User.java
            │   ├── Buyer.java
            │   ├── Seller.java
            │   ├── LogisticsOperator.java
            │   ├── Administrator.java
            │   ├── Supervisor.java
            │   ├── Warehouse.java
            │   ├── Product.java
            │   ├── PhysicalProduct.java
            │   ├── DigitalProduct.java
            │   ├── ProductVariant.java
            │   ├── Inventory.java
            │   ├── InventoryMovement.java
            │   ├── ShoppingCart.java
            │   ├── CartItem.java
            │   ├── TrackableProcess.java
            │   ├── Order.java
            │   ├── OrderItem.java
            │   ├── Invoice.java
            │   ├── Shipment.java
            │   ├── Return.java
            │   ├── Refund.java
            │   ├── Operation.java
            │   └── AuditLog.java
            │
            ├── valueObjects/
            │   ├── UserRole.java
            │   ├── UserStatus.java
            │   ├── BuyerCommercialStatus.java
            │   ├── WarehouseType.java
            │   ├── WarehouseStatus.java
            │   ├── ProductType.java
            │   ├── ProductStatus.java
            │   ├── InventoryMovementType.java
            │   ├── InventoryConditionStatus.java
            │   ├── OrderStatus.java
            │   ├── ShipmentStatus.java
            │   ├── ReturnStatus.java
            │   ├── RefundStatus.java
            │   ├── OperationType.java
            │   ├── ApprovalDecision.java
            │   ├── NotificationChannel.java
            │   └── AuditSeverity.java
            │
            ├── exceptions/
            │   ├── DomainException.java
            │   ├── EntityNotFoundException.java
            │   ├── InsufficientStockException.java
            │   ├── InvalidOrderStatusException.java
            │   ├── InvalidStatusTransitionException.java
            │   ├── UnauthorizedOperationException.java
            │   └── UserNotEligibleException.java
            │
            ├── services/
            │   ├── user/
            │   ├── seller/
            │   ├── buyer/
            │   ├── warehouse/
            │   ├── catalog/
            │   ├── inventory/
            │   ├── cart/
            │   ├── order/
            │   ├── invoicing/
            │   ├── logistics/
            │   ├── returns/
            │   ├── operation/
            │   └── authorization/
            │
            ├── ports/
            │   ├── in/
            │   │   ├── RegisterSellerUseCase.java
            │   │   ├── ConfirmOrderUseCase.java
            │   │   ├── DispatchOrderUseCase.java
            │   │   ├── RequestReturnUseCase.java
            │   │   ├── ProcessRefundUseCase.java
            │   │   └── ...
            │   │
            │   └── out/
            │       ├── UserRepositoryPort.java
            │       ├── BuyerRepositoryPort.java
            │       ├── SellerRepositoryPort.java
            │       ├── WarehouseRepositoryPort.java
            │       ├── ProductRepositoryPort.java
            │       ├── InventoryRepositoryPort.java
            │       ├── InventoryMovementRepositoryPort.java
            │       ├── ShoppingCartRepositoryPort.java
            │       ├── OrderRepositoryPort.java
            │       ├── InvoiceRepositoryPort.java
            │       ├── ShipmentRepositoryPort.java
            │       ├── ReturnRepositoryPort.java
            │       ├── RefundRepositoryPort.java
            │       ├── OperationRepositoryPort.java
            │       ├── AuditLogRepositoryPort.java
            │       ├── PasswordServicePort.java
            │       ├── JwtServicePort.java
            │       ├── NotificationPort.java
            │       ├── AuthorizationPort.java
            │       └── BusinessConfigurationPort.java
            │
            ├── adapters/
            │   ├── in/
            │   │   └── rest/
            │   │       ├── controllers/
            │   │       ├── requests/
            │   │       ├── responses/
            │   │       └── mappers/
            │   │
            │   └── out/
            │       └── persistence/
            │           ├── mysql/
            │           │   ├── entities/
            │           │   ├── repositories/
            │           │   ├── mappers/
            │           │   └── adapters/
            │           │
            │           └── mongodb/
            │               ├── documents/
            │               ├── repositories/
            │               ├── mappers/
            │               └── adapters/
            │
            └── infrastructure/
                ├── config/
                ├── database/
                └── security/
```

---

# Layer Responsibilities

## Application

The `application` package represents the root of the project.

It contains the application entry point (`NexMarketApplication.java`, already present in the repository) and all architectural components.

### Responsibilities

- Application bootstrap.
- Component organization.
- Dependency composition.

---

## NexMarketApplication.java

### Description

`NexMarketApplication.java` is the application's entry point, generated by Spring Initializr.

### Responsibilities

- Initialize the Spring Boot application context.
- Load infrastructure configuration.
- Configure dependency injection.
- Start the embedded REST server.

---

# Domain

The Domain layer is the core of the application. It contains all business rules described in the Functional Specification and must remain independent from any external technology.

No class inside `domains/`, `valueObjects/`, `services/`, `ports/`, or `exceptions/` may depend on:

- Spring
- JPA
- MongoDB
- Spring Security
- HTTP
- REST
- JSON
- SQL

This is consistent with the existing `domains/User.java`, which is a plain POJO using only Lombok for boilerplate — no persistence annotations.

---

## domains/

Contains the business entities described in the `Domain Model.md` document: identity (`User` and its specializations), catalog (`Product`, `Warehouse`, `Inventory`), commercial processes (`ShoppingCart`, `Order`, `Invoice`, `Shipment`, `Return`, `Refund`) and traceability (`Operation`, `AuditLog`).

These objects represent the marketplace business, independent of how they are persisted or exposed.

---

## valueObjects/

Represent immutable business concepts, currently implemented as plain Java enums (e.g. `UserRole`, `OrderStatus`, `ProductType`), consistent with the existing `UserRole.java` and `UserStatus.java`.

Value Objects are compared by value instead of identity.

---

## exceptions/

Contains business exceptions raised when a domain rule described in the specification is violated.

Examples:

- `InsufficientStockException` — raised when `Inventory.availableQuantity` would become negative.
- `InvalidOrderStatusException` — raised when attempting to modify an `Order` with `orderStatus = DELIVERED`.
- `UnauthorizedOperationException` — raised when a user attempts to act outside the scope of its `UserRole` (`RG-02`, `RG-03`).

Business exceptions belong exclusively to the domain.

---

## services/

Contain the business logic described in `Domain Services.md`, organized by subdomain (`user`, `seller`, `buyer`, `warehouse`, `catalog`, `inventory`, `cart`, `order`, `invoicing`, `logistics`, `returns`, `operation`, `authorization`).

Examples:

- `ConfirmOrderService`
- `ReserveInventoryService`
- `ProcessRefundService`
- `RegisterOperationAndAuditService`

Services coordinate business operations across multiple entities while preserving domain integrity, and generate `Operation`/`AuditLog` records for every significant transition of a `TrackableProcess`.

---

## ports/

Ports define communication contracts between the domain and external technologies. The domain owns all interfaces.

### Input Ports (`ports/in/`)

Represent application use cases, directly traceable to the service catalog in `Domain Services.md`.

Examples:

- `RegisterSellerUseCase`
- `ConfirmOrderUseCase`
- `DispatchOrderUseCase`
- `RequestReturnUseCase`
- `ProcessRefundUseCase`

Input ports define what the system can do.

### Output Ports (`ports/out/`)

Represent dependencies required by the domain, as defined in `Output-ports.md`.

Examples:

- `OrderRepositoryPort`
- `InventoryRepositoryPort`
- `AuditLogRepositoryPort`
- `NotificationPort`

Output ports define what the domain needs from external systems.

---

# Adapters

The adapters connect external technologies with the business domain. Adapters translate external requests into domain operations and transform domain objects into technology-specific representations. The domain never communicates directly with external systems.

---

## Input Adapters (`adapters/in/rest/`)

Expose the application to external clients (buyers, sellers, administrators, logistics operators, and supervisors interacting through client applications).

### Responsibilities

- Receive HTTP requests.
- Validate incoming data.
- Convert Request DTOs into Domain Models.
- Execute application use cases (Input Ports).
- Convert domain results into Response DTOs.

### Controllers

Expose REST endpoints (e.g. `/orders`, `/products`, `/inventory`, `/returns`). Controllers must never implement business rules — they delegate execution to the domain through Input Ports.

### Requests / Responses

DTOs that transport data in and out of the application. They must not contain business logic.

### Mappers

Convert between Request DTO ↔ Domain Model and Domain Model ↔ Response DTO, preventing the domain from depending on transport objects.

---

## Output Adapters (`adapters/out/persistence/`)

Connect the domain with external resources. This matches the two persistence dependencies already declared in `pom.xml`: `spring-boot-starter-data-jpa` (MySQL) and `spring-boot-starter-mongodb`.

```text
Persistence
├── MySQL
└── MongoDB
```

### MySQL Adapter

Responsible for relational persistence of the transactional business entities: `User` and its specializations, `Warehouse`, `Product`, `Inventory`, `InventoryMovement`, `ShoppingCart`, `Order`, `Invoice`, `Shipment`, `Return`, `Refund`, and `Operation`.

#### Components

- **Entities** — represent relational database tables (e.g. `OrderEntity`, `InventoryEntity`).
- **Repositories** — Spring Data JPA repositories.
- **Mappers** — convert Domain Models into database entities and back.
- **Adapters** — implement the corresponding Domain Output Port (e.g. `OrderRepositoryAdapter implements OrderRepositoryPort`).

A single Output Port may be backed by more than one table (e.g. `ProductRepositoryPort` may persist `Product`, `PhysicalProduct`, `DigitalProduct` and `ProductVariant` across several tables) without the domain being aware of it.

### MongoDB Adapter

Responsible for storing audit information, matching the traceability objective stated in the Functional Specification's introduction.

#### Components

- **Documents** — represent MongoDB collections (`AuditLogDocument`).
- **Repositories** — Spring Data MongoDB repositories.
- **Mappers** — convert `AuditLog` domain objects into MongoDB documents.
- **Adapters** — implement `AuditLogRepositoryPort`.

---

# Infrastructure

Infrastructure contains technical configuration required by the application. It does not contain business logic.

## Config

Application-wide configuration: REST configuration, serialization, environment/profile configuration.

## Database

MySQL and MongoDB connection configuration, matching the `application.properties` file and the JPA/Mongo starters declared in `pom.xml`.

## Security

Authentication and authorization configuration, matching `spring-boot-starter-security`:

- JWT configuration.
- Password encoder configuration.
- Authentication filters enforcing `UserRole`-based access (`RG-02`, `RG-03`).

---

# Dependency Flow

Dependencies always point toward the domain.

```text
REST Controller
        │
        ▼
Input Port
        │
        ▼
Domain Service
        │
        ▼
Output Port
        │
        ▼
Persistence Adapter
        │
        ▼
MySQL / MongoDB
```

For example, confirming an order:

```text
OrderController
        │
        ▼
ConfirmOrderUseCase
        │
        ▼
ConfirmOrderService
        │
        ├──> OrderRepositoryPort ──> OrderRepositoryAdapter ──> MySQL
        │
        └──> AuditLogRepositoryPort ──> AuditLogRepositoryAdapter ──> MongoDB
```

The domain never depends on adapters or infrastructure.

---

# Benefits

This architecture provides:

- Technology independence.
- High maintainability.
- Clear separation of concerns.
- Improved testability.
- Easier scalability as new subdomains (e.g. payments, promotions) are added.
- Better support for Domain-Driven Design.
- Easy replacement of frameworks or databases.
- Reusable business logic across REST, batch, or messaging entry points.
- Long-term maintainability.

---

# Architectural Constraints

The following rules must always be respected:

1. Business logic belongs exclusively to the Domain layer (`domains/`, `valueObjects/`, `services/`, `exceptions/`).
2. Controllers must not contain business rules.
3. DTOs must never enter the Domain layer.
4. Persistence entities and MongoDB documents must never be exposed through the API.
5. Communication between technologies and the Domain must occur only through Ports.
6. Adapters implement Ports but never define business rules.
7. Infrastructure depends on the Domain, never the opposite.
8. Every dependency must point toward the Domain.
9. Business entities must remain framework-independent (no JPA, Mongo, or Lombok-persistence annotations beyond plain `@Getter`/`@Setter`).
10. The Domain must be fully testable without requiring MySQL, MongoDB, or Spring Security.

# Persistence Adapters Specification (Output Adapters)

## 1. Overview

Persistence Adapters reside in `application/adapters/persistence/`. They implement the **Output Ports** defined in `application/domain/ports/out/`.

To guarantee complete technology independence and prevent persistence entities/ORM leaks into the domain:
1. Persistence adapters do NOT use domain models directly as database entities or documents.
2. Every output adapter defines its own **Persistence Entities / Documents**, **Mappers** (Domain Model ↔ Entity/Document), and **Spring Data Repositories**.
3. **Dual Database Architecture:**
   - **Relational (MySQL / Spring Data JPA):** Transactional entities (`User`, `Product`, `Order`, `Inventory`, `Warehouse`, `Shipment`, `Return`, `Refund`).
   - **NoSQL (MongoDB / Spring Data MongoDB):** Traceability and audit logs (`AuditLogDocument`).

---

## 2. Architecture & Data Flow

```text
Domain Service (domain/services)
      |
      v  Calls Output Port Interface
Output Port Interface (domain/ports/out)
      ^
      |  Implemented by Output Persistence Adapter
Persistence Adapter (adapters/persistence)
      |  1. Maps Domain Model -> Repository Entity/Document
      |  2. Calls Spring Data Repository
      v
Spring Data Repository (JPA / MongoDB)
      |
      v
Database (MySQL / MongoDB)
```

---

## 3. Package Structure

```text
application/adapters/persistence/
├── jpa/                                 # Relational Persistence (MySQL)
│   ├── entities/                        # JPA Entities (@Entity, @Table)
│   ├── mappers/                         # Domain <-> JPA Entity mappers
│   ├── repositories/                    # Spring Data JpaRepositories
│   └── UserJpaAdapter.java              # Implements UserRepositoryPort
│       OrderJpaAdapter.java             # Implements OrderRepositoryPort
│       ProductJpaAdapter.java           # Implements ProductRepositoryPort
│       InventoryJpaAdapter.java         # Implements InventoryRepositoryPort
│       ...
│
└── mongodb/                             # NoSQL Persistence (MongoDB)
    ├── documents/                       # Mongo Documents (@Document)
    ├── mappers/                         # Domain <-> Document mappers
    ├── repositories/                    # Spring Data MongoRepositories
    └── AuditLogMongoAdapter.java        # Implements AuditLogRepositoryPort
```

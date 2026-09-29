# REST Adapters Specification

## 1. Overview

REST Adapters reside in `application/adapters/rest/`. They handle HTTP transport concerns, receiving client requests, validating HTTP DTOs, mapping requests to Domain Models, evaluating authentication and user roles, calling the corresponding **Role Input Port**, and mapping Domain Models back to HTTP Response DTOs.

---

## 2. Component Architecture

```text
HTTP Request (Client)
      |
      v  Contains JWT Bearer Token
Spring Security Filter Chain
      |  Validates JWT & Loads User Domain Model
      v
REST Controller (adapters/rest/controllers)
      |  1. Maps Request DTO -> Domain Model
      |  2. Calls Role Input Port passing (User + Domain Model)
      v
Role Input Port Interface (domain/ports/in/*Port)
      ^
      |  Implemented by UseCase in adapters/useCases/
Use Case Implementation
```

---

## 3. JWT & Authentication Flow

1. **Login (`/api/v1/public/auth/login`):** Validates credentials through `PublicAccessPort.login()` and generates signed JWT containing user ID and role.
2. **Authenticated Requests:**
   - `JwtAuthenticationFilter` extracts the bearer token.
   - Reconstructs `User` domain model through `UserRepositoryPort`.
   - Populates Spring Security context with `AuthenticatedUserPrincipal`.
   - The REST Controller retrieves the principal and passes the authoritative `User` model to the Role Input Port.

---

## 4. Package Structure

```text
application/adapters/rest/
├── controllers/          # Endpoints grouped by Role (BuyerController, SellerController, etc.)
├── dtos/
│   ├── requests/         # Incoming JSON payloads
│   └── responses/        # Outgoing JSON payloads
├── exception/            # GlobalExceptionHandler and ErrorResponse
└── mappers/              # DTO <-> Domain Model mappers
```

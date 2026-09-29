# Global Exception Handler Specification (Spring Boot)

## 1. Purpose

This document defines the single, standardized error contract for all REST endpoints and the Spring Security boundary within the **NexusMarket Marketplace Information Management System**. Domain exceptions remain completely framework-independent; the REST adapter layer maps them to client-safe HTTP responses.

---

## 2. Standard Error Response

All handled errors return the standard JSON envelope:

```json
{
  "timestamp": "2026-09-29T10:00:00.000Z",
  "status": 404,
  "code": "RESOURCE_NOT_FOUND",
  "message": "Requested product was not found in catalog",
  "path": "/api/v1/public/catalog/PROD-123",
  "requestId": "req-01H...",
  "details": null
}
```

* `code`: Stable, machine-readable uppercase string (e.g., `INSUFFICIENT_STOCK`, `UNAUTHORIZED_OPERATION`, `DELIVERED_ORDER_IMMUTABLE`). Clients must never parse the free-form `message`.
* `message`: Human-readable explanation suitable for client display.
* `path`: Request URI without query parameters.
* `requestId`: Unique correlation identifier (`X-Request-Id`).
* Unexpected errors return generic messages (`INTERNAL_ERROR`) and never expose stack traces, SQL syntax, or internal infrastructure hostnames.

---

## 3. Marketplace Exception Mapping

| Failure / Exception | HTTP Status | Code |
| :--- | :---: | :--- |
| Malformed JSON, body parsing, binding violations | 400 | `INVALID_REQUEST` |
| `@Valid` DTO constraint violations (`@NotBlank`, `@Positive`) | 400 | `VALIDATION_FAILED` (with field details in `details`) |
| Invalid credentials, expired/missing JWT token | 401 | `AUTHENTICATION_REQUIRED` / `INVALID_CREDENTIALS` |
| `UnauthorizedOperationException` (role or domain boundary violation, `RG-02`, `RG-03`) | 403 | `FORBIDDEN` / `UNAUTHORIZED_OPERATION` |
| `EntityNotFoundException` (User, Product, Order, Warehouse not found) | 404 | `RESOURCE_NOT_FOUND` |
| Unique constraint collision (duplicate identifier or email) | 409 | `RESOURCE_ALREADY_EXISTS` |
| `InsufficientStockException` (Stock unavailable, damaged, or insufficient) | 409 | `INSUFFICIENT_STOCK` |
| `InvalidOrderStatusException` (e.g. attempting to modify a `DELIVERED` order) | 409 | `INVALID_ORDER_STATUS` |
| `InvalidStatusTransitionException` (e.g. illegal warehouse, refund, or return transition) | 409 | `INVALID_STATUS_TRANSITION` |
| `UserNotEligibleException` (Buyer commercial status is `BLOCKED` or `RESTRICTED`) | 422 | `USER_NOT_ELIGIBLE` |
| Database connectivity or external notification service failure | 503 | `DEPENDENCY_UNAVAILABLE` |
| Unhandled runtime exceptions | 500 | `INTERNAL_ERROR` |

---

## 4. Implementation Requirements

* Implement a centralized `@RestControllerAdvice` in `application/adapters/rest/exception/GlobalExceptionHandler.java`.
* Configure the Spring Security `AuthenticationEntryPoint` and `AccessDeniedHandler` to emit the exact same envelope for `401` and `403` exceptions originating within filter chains.
* Preserve the correlation header `X-Request-Id` across all responses and server-side logs.

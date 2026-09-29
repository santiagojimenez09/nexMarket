# REST Validation Specification (Spring Boot)

## 1. Purpose

This document defines incoming request payload validation rules at the REST boundary for **NexusMarket**. DTO validation guarantees transport integrity and data shape before requests reach the domain layer.

---

## 2. Processing Order

1. **Transport:** Method, URI, JSON media type parsing.
2. **Security:** Spring Security filter validates JWT, active user status, and role authority.
3. **DTO Shape & Bean Validation:** `@Valid` checks required fields, string limits, regex, and positive numbers.
4. **Domain Services:** Evaluates authoritative business rules (e.g. non-negative stock, unalterable delivered orders, return windows).

---

## 3. Boundary Validation Rules

| Field | Validation Constraint | Description |
| :--- | :--- | :--- |
| `email` | `@NotBlank`, `@Email` | Valid RFC email address. |
| `password` | `@NotBlank`, `@Size(min=8, max=100)` | Password for authentication. |
| `fullName` / `name` | `@NotBlank`, `@Size(min=2, max=150)` | Participant or catalog product name. |
| `primaryAddress` | `@NotBlank`, `@Size(min=5, max=250)` | Buyer delivery location. |
| `identifier` / `sku` | `@NotBlank`, `@Size(min=3, max=50)` | Business codes and SKUs. |
| `quantity` | `@NotNull`, `@Positive` | Units for cart, stock, or orders (integer > 0). |
| `price` / `amount` | `@NotNull`, `@DecimalMin("0.01")` | Monetary quantities. |
| `weight` | `@NotNull`, `@Positive` | Mandatory for physical products. |
| `deliveryAsset` | `@NotBlank` | Mandatory download URL or key for digital products. |
| `reason` | `@NotBlank`, `@Size(min=5, max=500)` | Justification for return or rejection. |

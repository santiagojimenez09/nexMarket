# REST CORS and Browser Security Specification (Spring Security)

## 1. Purpose

This document defines the browser security and Cross-Origin Resource Sharing (CORS) boundary for clients consuming the **NexusMarket API**.

---

## 2. CORS Policy

- **Allowed Origins:** Only the explicitly configured `FRONTEND_ORIGIN` (development default: `http://localhost:5173`).
- **Production Origins:** Must be strictly configured via environment variables and never fall back to wildcards (`*`).
- **Allowed HTTP Methods:** `GET`, `POST`, `PUT`, `PATCH`, `DELETE`, and `OPTIONS`.
- **Allowed Headers:** `Authorization`, `Content-Type`, `Accept`, and `X-Request-Id`.
- **Exposed Headers:** `X-Request-Id`.
- **Credentials:** `allowCredentials = false` while tokens are transmitted via the `Authorization: Bearer <token>` header.
- **Preflight Handling:** Accept CORS preflight `OPTIONS` with `204 No Content` without requiring JWT tokens.

---

## 3. Spring Security Configuration

* Centrally configured through `SecurityFilterChain` in `application/infrastructure/security/SecurityConfig.java` using a dedicated `CorsConfigurationSource`.
* CORS works in tandem with the JWT authentication filter: preflight requests pass through, while all transactional endpoints enforce role-based authorization.

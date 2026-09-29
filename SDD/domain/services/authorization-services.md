# Authorization Services Specification

## 1. Overview
Defines domain services for user authorization, RBAC validation, and access control across domain entities and trackable processes (`DOMINIO 13`, `OBJ-02`, `RG-02`, `RG-03`).

## 2. Services
- `ValidatePermissionsService`: Enforces role-based access control, active status checks, buyer data ownership verification, and trackable process boundary authorization.

## 3. Invariants & Business Rules
- Unauthenticated or non-active users are rejected (`RG-02`).
- Actions requiring specific privileges throw `UnauthorizedOperationException` if roles mismatch (`RG-03`).
- Output Ports used: `AuthorizationPort`.

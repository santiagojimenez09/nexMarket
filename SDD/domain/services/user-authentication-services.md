# User & Authentication Services Specification

## 1. Overview
Defines domain services for participant identity and authentication (`OBJ-01`, `RG-02`, `RG-03`).

## 2. Services
- `RegisterUserService`: Validates uniqueness of identifier and email, hashes passwords via `PasswordServicePort`.
- `ConsultUserService`: Retrieves user by identifier or email.
- `UpdateUserService`: Updates user profile information.
- `ChangeUserStatusService`: Transitions user operational status (`ACTIVE`, `INACTIVE`, `BLOCKED`).
- `AuthenticateUserService`: Validates credentials, verifies active status, and issues tokens via `JwtServicePort`.

## 3. Invariants & Business Rules
- `RG-02`: Every user has exactly one business role (`UserRole`).
- Blocked (`BLOCKED`) or inactive (`INACTIVE`) users cannot perform transactions.
- Output Ports used: `UserRepositoryPort`, `PasswordServicePort`, `JwtServicePort`.

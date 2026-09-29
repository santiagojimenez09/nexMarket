# Operation and Audit Services Specification

## 1. Overview
Defines domain services for operation tracking, immutable audit trails, and administrative reporting (`DOMINIO 12`, `OBJ-12`).

## 2. Services
- `RegisterOperationAndAuditService`: Records business operations (`Operation`) and an immutable, append-only audit trail (`AuditLog`) capturing actor, timestamps, affected trackable processes, and operational details.
- `ConsultAuditLogService`: Queries the immutable audit trail filtered by user, trackable process, or operation type.
- `GenerateAdministrativeReportService`: Generates summarized administrative operational metrics (total orders created, dispatched, returned) strictly restricted to `SUPERVISOR` or `ADMINISTRATOR` roles.

## 3. Invariants & Business Rules
- Audit logs (`AuditLog`) are strictly append-only; updates and deletions are forbidden.
- Administrative report generation enforces role verification (`SUPERVISOR` / `ADMINISTRATOR`).
- Output Ports used: `OperationRepositoryPort`, `AuditLogRepositoryPort`.

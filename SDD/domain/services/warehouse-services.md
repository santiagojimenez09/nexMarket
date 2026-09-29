# Warehouse Services Specification

## 1. Overview
Defines domain services for storage facility administration (`DOMINIO 4`, `OBJ-04`).

## 2. Services
- `RegisterWarehouseService`: Creates a warehouse classified as `MARKETPLACE` (no owner) or `SELLER` (owner mandatory).
- `ConsultWarehouseService`: Retrieves warehouse details by identifier, seller, or type.
- `ChangeWarehouseStatusService`: Transitions operational status (`ACTIVE`, `INACTIVE`).

## 3. Invariants & Business Rules
- If `type == SELLER`, `owner` must be non-null.
- If `type == MARKETPLACE`, `owner` must be null.
- Output Ports used: `WarehouseRepositoryPort`.

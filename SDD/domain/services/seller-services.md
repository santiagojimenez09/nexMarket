# Seller Services Specification

## 1. Overview
Defines domain services for seller merchant incorporation and management (`DOMINIO 3`, `OBJ-02`).

## 2. Services
- `RegisterSellerService`: Incorporates a seller by an Administrator and creates their initial warehouse. Sellers cannot self-register (`DOMINIO 3`).
- `ConsultSellerService`: Retrieves seller profile and owned warehouses.
- `ChangeSellerStatusService`: Modifies seller active/suspended status.
- `ConsultSellerProductsService`: Lists products administered by a seller.

## 3. Invariants & Business Rules
- Only an `ADMINISTRATOR` can register a `Seller`.
- Output Ports used: `SellerRepositoryPort`, `UserRepositoryPort`, `WarehouseRepositoryPort`, `ProductRepositoryPort`.

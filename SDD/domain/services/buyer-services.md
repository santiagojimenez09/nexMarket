# Buyer Services Specification

## 1. Overview
Defines domain services for buyer participant administration (`DOMINIO 2`, `OBJ-03`).

## 2. Services
- `RegisterBuyerService`: Sets up initial address, role `BUYER`, and status `ACTIVE`.
- `ConsultBuyerService`: Retrieves buyer profile ensuring buyers cannot view other buyers' information (`DOMINIO 2`).
- `UpdateBuyerAddressesService`: Updates primary and secondary addresses.
- `ChangeBuyerCommercialStatusService`: Modifies standing (`ACTIVE`, `RESTRICTED`, `BLOCKED`).
- `ConsultBuyerOrdersService`: Retrieves orders placed by the buyer on demand.

## 3. Invariants & Business Rules
- Buyers never administer data belonging to other buyers or sellers.
- Output Ports used: `BuyerRepositoryPort`, `UserRepositoryPort`, `OrderRepositoryPort`.

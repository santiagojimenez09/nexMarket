# Inventory Services Specification

## 1. Overview
Defines domain services for distributed stock management (`DOMINIO 6`, `OBJ-06`).

## 2. Services
- `RegisterInventoryService`: Links a physical product to a warehouse with initial stock.
- `ReserveInventoryService`: Validates non-negative stock and excludes `DAMAGED` stock to reserve units for an order.
- `RegisterInboundMovementService`: Increases stock and generates `InventoryMovement` (`INBOUND`).
- `RegisterSaleOutboundMovementService`: Decreases reserved stock and records `SALE_OUTBOUND`.
- `RegisterInventoryAdjustmentService`: Adjusts stock while enforcing the invariant `availableQuantity >= 0`.
- `RegisterReturnInboundMovementService`: Reincorporates returned units into stock.

## 3. Invariants & Business Rules
- `availableQuantity` must never be negative under any circumstance.
- Stock in `DAMAGED` condition cannot be reserved or sold.
- Every stock change must produce an immutable `InventoryMovement`.
- Output Ports used: `InventoryRepositoryPort`, `InventoryMovementRepositoryPort`.

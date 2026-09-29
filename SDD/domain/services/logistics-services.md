# Logistics Services Specification

## 1. Overview
Defines domain services for packaging, transportation, and delivery (`DOMINIO Envíos`, `OBJ-10`).

## 2. Services
- `PrepareShipmentService`: Packages physical items of an order, assigns operator and origin warehouse (`IN_PREPARATION`).
- `DispatchShipmentService`: Records physical departure from warehouse (`DISPATCHED`).
- `ConfirmShipmentDeliveryService`: Marks shipment as `DELIVERED` and triggers order delivery finalization.
- `ConsultShipmentService`: Tracks shipment progress.

## 3. Invariants & Business Rules
- Shipments can only be created for paid orders (`PAID`).
- Output Ports used: `ShipmentRepositoryPort`, `OrderRepositoryPort`, `WarehouseRepositoryPort`.

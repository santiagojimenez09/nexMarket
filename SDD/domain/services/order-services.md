# Order Services Specification

## 1. Overview
Defines domain services for order lifecycle management (`DOMINIO 7`, `OBJ-08`).

## 2. Services
- `ConfirmOrderService`: Converts active `ShoppingCart` into `Order` in `PENDING_PAYMENT` status. Validates buyer status (`BuyerCommercialStatus.ACTIVE`).
- `ConfirmPaymentService`: Transitions order to `PAID`.
- `DispatchOrderService`: Transitions order to `DISPATCHED`.
- `ConfirmDeliveryService`: Transitions order to `DELIVERED`.

## 3. Invariants & Business Rules
- Orders in `DELIVERED` status are strictly immutable and can never be modified.
- Orders can only transition sequentially: `PENDING_PAYMENT` -> `PAID` -> `DISPATCHED` -> `DELIVERED`.
- Output Ports used: `OrderRepositoryPort`, `ShoppingCartRepositoryPort`.

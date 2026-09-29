# Return and Refund Services Specification

## 1. Overview
Defines domain services for post-sale returns and monetary refunds (`DOMINIO Devoluciones`, `DOMINIO Reembolsos`, `OBJ-11`).

## 2. Services
- `RequestReturnService`: Creates a return request for delivered orders within the business configuration return window (`BusinessConfigurationPort`).
- `ApproveReturnService`: Approves return request (`ReturnStatus.APPROVED`).
- `RejectReturnService`: Denies return request with required reason.
- `ProcessRefundService`: Issues monetary refund for approved returns processed by an `Administrator`.
- `RejectRefundService`: Denies refund request.

## 3. Invariants & Business Rules
- Returns can only be requested for orders in `DELIVERED` status.
- Refunds can only be issued for returns in `APPROVED` status.
- Only an `ADMINISTRATOR` can process or reject refunds.
- Output Ports used: `ReturnRepositoryPort`, `RefundRepositoryPort`, `OrderRepositoryPort`, `BusinessConfigurationPort`.

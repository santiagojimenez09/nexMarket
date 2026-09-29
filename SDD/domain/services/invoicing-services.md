# Invoicing Services Specification

## 1. Overview
Defines domain services for commercial billing (`DOMINIO Facturación`, `OBJ-09`).

## 2. Services
- `IssueInvoiceService`: Generates an invoice for a paid order, computing total amounts from order items.
- `ConsultInvoiceService`: Retrieves invoices by order or buyer.

## 3. Invariants & Business Rules
- Invoices can only be issued for orders in `PAID`, `DISPATCHED`, or `DELIVERED` status. Never for `PENDING_PAYMENT`.
- Output Ports used: `InvoiceRepositoryPort`, `OrderRepositoryPort`.

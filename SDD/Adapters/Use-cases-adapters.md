# Use Cases Adapters Specification

## 1. Overview

Use Case Adapters reside in `application/adapters/useCases/`. They serve as the concrete implementations of the **Role Input Ports** defined in `application/domain/ports/in/`.

Use Case Adapters bridge incoming application calls (from REST Controllers or other entry points) to the core **Domain Services** (`application/domain/services/`).

---

## 2. Architectural Role and Injections

Every Use Case Adapter:
1. Implements a specific Role Input Port interface (`domain/ports/in/*Port`).
2. **Injects the concrete Domain Service classes** that contain the business logic, domain validations, and Output Port connections.

```text
REST Controller (adapters/rest/controllers)
      |
      v  Calls Input Port Interface
Role Input Port (domain/ports/in/*Port)
      ^
      |  Implemented by
Use Case Adapter (adapters/useCases/*UseCaseImpl)
      |
      v  Injects & Delegates to
Domain Service (domain/services/*Service)
      |
      v  Calls Output Port Interface
Output Port (domain/ports/out/*Port)
```

---

## 3. Structure and Naming Conventions

| Role Input Port (`domain/ports/in/`) | Use Case Implementation Class (`adapters/useCases/`) | Injected Domain Services (`domain/services/`) |
| :--- | :--- | :--- |
| `PublicAccessPort` | `PublicAccessUseCaseImpl` | `AuthenticateUserService`, `RegisterBuyerService`, `ConsultPublicCatalogService`, `ConsultProductService` |
| `BuyerPort` | `BuyerUseCaseImpl` | `ConsultBuyerService`, `UpdateBuyerAddressesService`, `ConsultCartService`, `AddItemToCartService`, `UpdateCartItemService`, `RemoveItemFromCartService`, `ClearCartService`, `ConfirmOrderService`, `ConsultOrderService`, `ConsultBuyerOrdersService`, `ConsultInvoiceService`, `RequestReturnService` |
| `SellerPort` | `SellerUseCaseImpl` | `ConsultSellerService`, `RegisterProductService`, `PublishProductService`, `SuspendProductService`, `DiscontinueProductService`, `ConsultSellerProductsService`, `ConsultWarehouseService`, `ConsultInventoryService` |
| `LogisticsOperatorPort` | `LogisticsOperatorUseCaseImpl` | `ConsultWarehouseService`, `ConsultInventoryService`, `RegisterInboundMovementService`, `RegisterInventoryAdjustmentService`, `RegisterReturnInboundMovementService`, `PrepareShipmentService`, `DispatchShipmentService`, `ConfirmShipmentDeliveryService`, `ConsultShipmentService` |
| `AdministratorPort` | `AdministratorUseCaseImpl` | `RegisterUserService`, `ChangeUserStatusService`, `ChangeBuyerCommercialStatusService`, `RegisterSellerService`, `ChangeSellerStatusService`, `RegisterWarehouseService`, `ChangeWarehouseStatusService`, `ApproveReturnService`, `RejectReturnService`, `ProcessRefundService`, `RejectRefundService` |
| `SupervisorPort` | `SupervisorUseCaseImpl` | `GenerateAdministrativeReportService`, `ConsultAuditLogService`, `ConsultOperationsService`, `ConsultOrderService` |

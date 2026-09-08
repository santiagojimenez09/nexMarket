# Domain Value Objects

## Introduction

Value Objects represent immutable concepts within the NexusMarket marketplace domain.

Unlike Entities, Value Objects do not have their own identity. They are defined entirely by their values and are used to encapsulate controlled business concepts, improve domain expressiveness, and prevent the use of primitive values or scattered string literals throughout the application.

The marketplace domain uses Value Objects for business catalogs such as roles, statuses, product types, movement types, and operation types.

All business catalogs inherit from `DomainCatalog`.

---

# Value Object Hierarchy

```text
DomainCatalog (Abstract)
├── UserRole
├── UserStatus
├── BuyerCommercialStatus
├── WarehouseType
├── WarehouseStatus
├── ProductType
├── ProductStatus
├── InventoryMovementType
├── InventoryConditionStatus
├── OrderStatus
├── ShipmentStatus
├── ReturnStatus
├── RefundStatus
└── OperationType
```

---

# DomainCatalog (Abstract)

## Description

Represents a generic business catalog used throughout the marketplace domain.

`DomainCatalog` provides a consistent structure for controlled business values that require a code, human-readable name, and business description.

This class cannot be instantiated directly.

## Attributes

| Attribute   | Type   | Description                                           |
| ----------- | ------ | ----------------------------------------------------- |
| code        | String | Unique business identifier of the catalog value.      |
| name        | String | Human-readable name displayed within the application. |
| description | String | Business definition of the catalog value.             |

## Characteristics

* Immutable.
* Equality is determined by value rather than object identity.
* Catalog values are controlled by the domain.
* Catalog values must not be represented by arbitrary strings throughout the application.
* Each catalog value must have a unique `code`.

---

# UserRole

## Description

Represents the responsibilities and permissions assigned to a user within the marketplace.

The role is a characteristic of `User` because it represents what the participant means within the system and the responsibilities associated with that participant (`RG-02`).

## Inherits From

`DomainCatalog`

## Allowed Values

| Code                | Name                | Description                                                       |
| -------------------- | -------------------- | ---------------------------------------------------------------------- |
| BUYER                | Buyer                | Purchases products published on the marketplace.                     |
| SELLER               | Seller               | Registers and administers products commercialized on the platform.    |
| LOGISTICS_OPERATOR   | Logistics Operator   | Operates warehouses and dispatches orders.                            |
| ADMINISTRATOR        | Administrator        | Administers sellers and warehouses.                                    |
| SUPERVISOR           | Supervisor           | Read-only consultation and operational oversight profile.              |

---

# UserStatus

## Description

Represents the current operational status of a user within the platform.

## Inherits From

`DomainCatalog`

## Allowed Values

| Code     | Name     | Description                                       |
| -------- | -------- | ------------------------------------------------- |
| ACTIVE   | Active   | User can access and operate the platform normally. |
| INACTIVE | Inactive | User exists but cannot currently perform operations. |
| BLOCKED  | Blocked  | User access has been suspended.                     |

---

# BuyerCommercialStatus

## Description

Represents the buyer's condition for participating in commercial processes.

`BuyerCommercialStatus` is independent from `UserStatus`. It represents the buyer's standing to place orders rather than the state of the buyer's platform access.

## Inherits From

`DomainCatalog`

## Allowed Values

| Code       | Name       | Description                                  |
| ---------- | ---------- | --------------------------------------------- |
| ACTIVE     | Active     | Buyer can place orders normally.              |
| RESTRICTED | Restricted | Buyer's ability to place orders is limited.   |
| BLOCKED    | Blocked    | Buyer cannot place orders.                    |

---

# WarehouseType

## Description

Represents the classification of a warehouse: owned by the Marketplace, or owned by a Seller.

## Inherits From

`DomainCatalog`

## Allowed Values

| Code        | Name        | Description                                       |
| ----------- | ----------- | --------------------------------------------------- |
| MARKETPLACE | Marketplace | Warehouse owned and operated by the Marketplace.    |
| SELLER      | Seller      | Warehouse owned and operated by a seller.           |

---

# WarehouseStatus

## Description

Represents the operational status of a warehouse.

## Inherits From

`DomainCatalog`

## Allowed Values

| Code     | Name     | Description                          |
| -------- | -------- | ------------------------------------- |
| ACTIVE   | Active   | Warehouse is fully operational.       |
| INACTIVE | Inactive | Warehouse is temporarily not in use.  |

---

# ProductType

## Description

Represents the discriminator between products that require physical inventory and dispatch, and products delivered digitally.

## Inherits From

`DomainCatalog`

## Allowed Values

| Code     | Name     | Description                                        |
| -------- | -------- | --------------------------------------------------- |
| PHYSICAL | Physical | Product requiring inventory and physical dispatch.  |
| DIGITAL  | Digital  | Product delivered immediately after payment.        |

---

# ProductStatus

## Description

Represents the current publication status of a product in the catalog.

## Inherits From

`DomainCatalog`

## Allowed Values

| Code         | Name         | Description                                     |
| ------------ | ------------ | ------------------------------------------------ |
| PUBLISHED    | Published    | Product is visible in the public catalog.        |
| SUSPENDED    | Suspended    | Product is temporarily hidden from the catalog.  |
| DISCONTINUED | Discontinued | Product has been permanently retired.             |

---

# InventoryMovementType

## Description

Represents the type of change applied to an inventory record.

## Inherits From

`DomainCatalog`

## Allowed Values

| Code           | Name           | Description                                             |
| -------------- | -------------- | --------------------------------------------------------- |
| INBOUND        | Inbound        | Stock entering the warehouse.                            |
| RESERVATION    | Reservation    | Stock reserved for a pending order.                       |
| SALE_OUTBOUND  | Sale Outbound  | Stock leaving the warehouse due to a confirmed sale.       |
| ADJUSTMENT     | Adjustment     | Manual correction of stock levels.                          |
| RETURN_INBOUND | Return Inbound | Stock re-entering the warehouse from an approved return.    |

---

# InventoryConditionStatus

## Description

Represents the physical condition of a unit of stock. Stock marked as `DAMAGED` cannot be reserved.

## Inherits From

`DomainCatalog`

## Allowed Values

| Code      | Name      | Description                             |
| --------- | --------- | ---------------------------------------- |
| AVAILABLE | Available | Stock is in sellable condition.          |
| DAMAGED   | Damaged   | Stock is damaged and cannot be reserved. |

---

# OrderStatus

## Description

Represents the stage of the order lifecycle.

## Inherits From

`DomainCatalog`

## Allowed Values

| Code            | Name            | Description                                            |
| --------------- | --------------- | -------------------------------------------------------- |
| CART            | Cart            | Provisional product selection, not yet confirmed.       |
| PENDING_PAYMENT | Pending Payment | Order confirmed; awaiting financial confirmation.        |
| PAID            | Paid            | Payment confirmed; preparation process starts.            |
| DISPATCHED      | Dispatched      | Order has physically left the warehouse.                  |
| DELIVERED       | Delivered       | Order delivery has been confirmed; order is finalized.     |

---

# ShipmentStatus

## Description

Represents the current stage of a shipment's logistics process.

## Inherits From

`DomainCatalog`

## Allowed Values

| Code           | Name           | Description                                    |
| -------------- | -------------- | ------------------------------------------------- |
| IN_PREPARATION | In Preparation | Items are being packaged for dispatch.           |
| DISPATCHED     | Dispatched     | Shipment has left the origin warehouse.           |
| IN_TRANSIT     | In Transit     | Shipment is being transported to the buyer.        |
| DELIVERED      | Delivered      | Shipment has been delivered to the buyer.          |

---

# ReturnStatus

## Description

Represents the current stage of a return request.

## Inherits From

`DomainCatalog`

## Allowed Values

| Code      | Name      | Description                                       |
| --------- | --------- | -------------------------------------------------- |
| REQUESTED | Requested | Return has been requested by the buyer.            |
| APPROVED  | Approved  | Return has been approved.                           |
| REJECTED  | Rejected  | Return has been denied.                             |
| COMPLETED | Completed | Returned items have been received and processed.    |

---

# RefundStatus

## Description

Represents the current stage of a refund.

## Inherits From

`DomainCatalog`

## Allowed Values

| Code      | Name      | Description                                    |
| --------- | --------- | ------------------------------------------------ |
| PENDING   | Pending   | Refund has been requested but not processed.     |
| PROCESSED | Processed | Refund has been paid out to the buyer.            |
| REJECTED  | Rejected  | Refund request has been denied.                    |

---

# OperationType

## Description

Represents the type of significant business operation executed within the marketplace.

Every significant operation generated by a trackable process must reference one operation type.

## Inherits From

`DomainCatalog`

## Allowed Values

| Code                 | Name                 | Description                                        |
| --------------------- | --------------------- | --------------------------------------------------------- |
| SELLER_REGISTERED     | Seller Registered     | Registration of a new seller.                            |
| WAREHOUSE_REGISTERED  | Warehouse Registered  | Registration of a new warehouse.                           |
| PRODUCT_PUBLISHED     | Product Published     | Publication of a product in the catalog.                   |
| INVENTORY_ADJUSTED    | Inventory Adjusted    | Manual correction of inventory levels.                       |
| ORDER_CREATED         | Order Created         | Confirmation of an order from a shopping cart.               |
| PAYMENT_CONFIRMED     | Payment Confirmed     | Confirmation of payment for an order.                          |
| ORDER_DISPATCHED      | Order Dispatched      | Physical dispatch of an order from the warehouse.               |
| ORDER_DELIVERED       | Order Delivered       | Confirmed delivery of an order to the buyer.                    |
| SHIPMENT_PREPARED     | Shipment Prepared     | Packaging of a shipment prior to dispatch.                       |
| SHIPMENT_DISPATCHED   | Shipment Dispatched   | Departure of a shipment from the origin warehouse.                |
| SHIPMENT_DELIVERED    | Shipment Delivered    | Confirmed delivery of a shipment.                                  |
| RETURN_REQUESTED      | Return Requested      | Submission of a return request by the buyer.                       |
| RETURN_APPROVED       | Return Approved       | Approval of a return request.                                       |
| RETURN_REJECTED       | Return Rejected       | Rejection of a return request.                                       |
| REFUND_PROCESSED      | Refund Processed      | Successful payout of a refund.                                       |
| REFUND_REJECTED       | Refund Rejected       | Denial of a refund request.                                          |
| INVOICE_ISSUED        | Invoice Issued        | Issuance of an invoice for a paid order.                              |

---

# Primitive Enumerations

The following concepts are simple enumerations because they represent fixed technical values without requiring business catalog metadata or domain identity.

---

## ApprovalDecision

### Description

Represents the result of an approval process (e.g. for a `Return`).

### Values

* APPROVED
* REJECTED

---

## NotificationChannel

### Description

Represents the communication channel used by the system to notify participants.

### Values

* EMAIL
* SMS
* PUSH_NOTIFICATION

---

## AuditSeverity

### Description

Represents the severity level of an audit event.

### Values

* INFORMATION
* WARNING
* ERROR
* CRITICAL

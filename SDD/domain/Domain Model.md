# Domain Model

## Introduction

The Domain Model represents the core business entities of the NexusMarket Marketplace Information Management System. These entities encapsulate the business rules, data, relationships, and lifecycle concepts described in the Functional Specification (`Especificación Funcional del Negocio - NexusMarket`).

The model follows Object-Oriented Design and Domain-Driven Design (DDD) principles. Inheritance is used to represent genuine domain specialization, while explicit object relationships are preferred over generic identifier fields.

The model distinguishes between:

* **Users**, which represent identifiable participants and their role within the platform.
* **Warehouses**, which represent the physical locations where inventory is administered.
* **Products**, which represent the goods offered through the catalog.
* **Inventory**, which represents distributed stock linked to a product and a warehouse.
* **Commercial Processes**, which represent the trackable, stateful business flows generated during a purchase (`Order`, `Shipment`, `Return`, `Refund`).
* **Operations**, which represent significant business actions performed over a commercial process.
* **Audit Logs**, which provide an immutable historical record of operations, supporting the traceability objective stated in the specification's introduction.

A commercial process may generate multiple operations throughout its lifecycle. Every significant business operation must be recorded in the audit trail.

---

# Domain Class Hierarchy

```text
User (Abstract)
├── Buyer
├── Seller
├── LogisticsOperator
├── Administrator
└── Supervisor

Product (Abstract)
├── PhysicalProduct
└── DigitalProduct

TrackableProcess (Abstract)
├── Order
├── Shipment
├── Return
└── Refund

Warehouse
Inventory
InventoryMovement
ShoppingCart
Invoice
Operation
AuditLog
```

---

# Domain Relationships

```text
User
   │
   ├── Buyer ──────────────> owns ──> ShoppingCart
   │      │
   │      └── places ──────> Order
   │
   ├── Seller ─────────────> registers ──> Product
   │      │
   │      └── owns ────────> Warehouse (SELLER type)
   │
   ├── LogisticsOperator ──> operates ──> Warehouse / Shipment
   │
   ├── Administrator ──────> registers ──> Seller, Warehouse
   │
   └── Supervisor ─────────> consults ──> Order, Inventory, Reports

Product
   │
   └── stocked in ─────────> Inventory ──> linked to ──> Warehouse

ShoppingCart
   │
   └── converted into ─────> Order

Order
   ├── contains ────────────> OrderItem(s) ──> Product
   ├── generates ───────────> Invoice
   ├── generates ───────────> Shipment
   └── may generate ────────> Return ──> Refund

TrackableProcess
   │
   └── generates ───────────> Operation ──> recorded in ──> AuditLog

Inventory
   └── generates ───────────> InventoryMovement
```

---

# Entities

---

# User (Abstract)

## Description

Represents any participant authorized to interact with the NexusMarket platform.

This abstract class centralizes the common identity information shared by every participant described in the specification (`Comprador`, `Vendedor`, `Operador Logístico`, `Administrador`, `Supervisor`).

The role assigned to a user represents what that person means within the system and determines the responsibilities or business capabilities associated with that participant. Each user has a single role and may only administer information within the scope of that role (`RG-02`, `RG-03`).

This class cannot be instantiated directly.

## Attributes

| Attribute      | Type       | Description                                                                                     |
| -------------- | ---------- | ------------------------------------------------------------------------------------------------- |
| identifier     | String     | Unique identifier of the user (document of identity). Must be unique across the platform.        |
| fullName       | String     | Full name of the user. Cannot be empty.                                                          |
| email          | String     | Primary email address, used for access and communication. Must be unique across the platform.    |
| role           | UserRole   | Business role that defines the user's responsibilities and permissions within the system.        |
| status         | UserStatus | Current operational status of the user (e.g. Active, Blocked).                                   |

## Relationships

* A `User` is specialized into exactly one of `Buyer`, `Seller`, `LogisticsOperator`, `Administrator`, or `Supervisor`.
* The `role` belongs to `User` because it represents the participant's meaning and responsibilities within the marketplace.

## Business Rule

```text
RG-02: Each user has exactly one role within the system.
RG-03: No participant may administer information outside the scope of its role.
```

---

# Buyer

## Description

Represents a user who purchases products published on the marketplace.

A buyer never administers information belonging to other buyers, nor inventory information, consistent with the restriction stated in the specification (`DOMINIO 2`).

## Inherits From

`User`

## Attributes

| Attribute            | Type              | Description                                                             |
| -------------------- | ----------------- | ------------------------------------------------------------------------ |
| primaryAddress       | String            | Habitual address used for deliveries. Required.                        |
| additionalAddresses  | List\<String\>    | Secondary delivery addresses. Empty by default.                        |
| commercialStatus     | BuyerCommercialStatus | Condition of the buyer with respect to its ability to place orders. |
| cart                 | ShoppingCart?      | Buyer's active shopping cart. Created on first use.                    |
| orders               | List\<Order\>      | Orders placed by the buyer. Empty by default.                          |

## Relationships

* A `Buyer` owns zero or one active `ShoppingCart`.
* A `Buyer` places zero or more `Order` instances.
* `orders` are not populated by default; they are loaded on demand by the **Consult Buyer Orders** service.

---

# Seller

## Description

Represents a user responsible for registering and administering the products it commercializes.

Sellers cannot self-register; they are incorporated into the platform by an `Administrator` (`DOMINIO 3`).

## Inherits From

`User`

## Attributes

| Attribute    | Type                 | Description                                                        |
| ------------ | -------------------- | -------------------------------------------------------------------- |
| registeredBy | Administrator        | Administrator who incorporated the seller into the platform.       |
| warehouses   | List\<Warehouse\>    | Warehouses owned and operated by the seller. Empty by default.     |
| products     | List\<Product\>      | Products registered and administered by the seller. Empty by default. |

## Relationships

* A `Seller` is registered by exactly one `Administrator`.
* A `Seller` owns zero or more `Warehouse` instances of type `SELLER`.
* A `Seller` registers zero or more `Product` instances.

---

# LogisticsOperator

## Description

Represents a user responsible for the physical operation of warehouses and the dispatch of orders.

## Inherits From

`User`

## Attributes

| Attribute            | Type                | Description                                                    |
| --------------------- | ------------------- | ---------------------------------------------------------------- |
| operatedWarehouses    | List\<Warehouse\>   | Warehouses whose physical operation is under this operator's charge. |

## Relationships

* A `LogisticsOperator` operates zero or more `Warehouse` instances.
* A `LogisticsOperator` may perform `InventoryMovement` and `Shipment` operations.

---

# Administrator

## Description

Represents a user responsible for administering sellers and warehouses.

## Inherits From

`User`

## Relationships

* An `Administrator` registers zero or more `Seller` instances.
* An `Administrator` registers zero or more `Warehouse` instances (typically of type `MARKETPLACE`).

---

# Supervisor

## Description

Represents a read-only, oversight profile used for operational consultation and reporting (`OBJ-12`).

A supervisor does not create, modify, or delete business information; it only consults it.

## Inherits From

`User`

---

# Warehouse

## Description

Represents a physical location where inventory is administered.

The specification distinguishes between warehouses that belong to the Marketplace itself and warehouses that belong to a specific seller (`DOMINIO 4`).

## Attributes

| Attribute | Type            | Description                                                                 |
| --------- | --------------- | ----------------------------------------------------------------------------- |
| identifier| String          | Unique identifier of the warehouse.                                         |
| name      | String          | Descriptive name of the warehouse.                                          |
| address   | String          | Physical location of the warehouse.                                         |
| type      | WarehouseType   | Classification of the warehouse (Marketplace or Seller).                    |
| owner     | Seller?         | Seller who owns the warehouse. Empty when `type = MARKETPLACE`.             |
| status    | WarehouseStatus | Current operational status of the warehouse.                                |

## Relationships

* A `Warehouse` may belong to zero or one `Seller` (empty for Marketplace warehouses).
* A `Warehouse` may be operated by one or more `LogisticsOperator` instances.
* A `Warehouse` holds zero or more `Inventory` records.

## Business Rule

```text
When type = SELLER, owner must be present.
When type = MARKETPLACE, owner must be empty.
```

---

# Product (Abstract)

## Description

Represents a good offered through the marketplace catalog.

The catalog differentiates between products that require inventory and dispatch (`PhysicalProduct`) and products that are delivered immediately after payment (`DigitalProduct`) (`DOMINIO 5`).

This class cannot be instantiated directly.

## Attributes

| Attribute    | Type            | Description                                                              |
| ------------ | --------------- | --------------------------------------------------------------------------- |
| identifier   | String          | Unique identifier (SKU) of the product.                                   |
| name         | String          | Commercial name of the product.                                          |
| description  | String          | Description of the product.                                              |
| seller       | Seller          | Seller who registered and administers the product.                       |
| variants     | List\<ProductVariant\> | Variations of the product (color, size, model, etc.). Empty by default. |
| productType  | ProductType     | Discriminator indicating whether the product is Physical or Digital.     |
| status       | ProductStatus   | Current publication status of the product.                               |

## Relationships

* A `Product` is registered by exactly one `Seller`.
* A `Product` may have zero or more `ProductVariant` values.
* A `PhysicalProduct` is stocked in zero or more `Inventory` records.

## Business Rule

```text
Only products with status = PUBLISHED are visible in the public catalog.
```

---

# ProductVariant

## Description

Represents a value object capturing a specific variation of a product, such as color, size, or model.

## Type

Value Object (embedded in `Product`)

## Attributes

| Attribute      | Type   | Description                                    |
| -------------- | ------ | ------------------------------------------------ |
| variantName    | String | Name of the variation dimension (e.g. "Color"). |
| variantValue   | String | Value of the variation (e.g. "Red").            |
| skuSuffix      | String | Identifier suffix distinguishing this variant.  |

---

# PhysicalProduct

## Description

Represents a tangible product that requires distributed inventory and physical dispatch to be delivered to the buyer.

## Inherits From

`Product`

## Attributes

| Attribute | Type       | Description                              |
| --------- | ---------- | ------------------------------------------ |
| weight    | BigDecimal | Shipping weight used for logistics purposes. |

## Relationships

* A `PhysicalProduct` requires one or more `Inventory` records to be sellable.

---

# DigitalProduct

## Description

Represents an intangible product delivered immediately to the buyer once payment is confirmed. Digital products do not require inventory or physical dispatch.

## Inherits From

`Product`

## Attributes

| Attribute       | Type   | Description                                             |
| --------------- | ------ | --------------------------------------------------------- |
| deliveryAsset   | String | Reference to the digital asset delivered upon payment.  |

---

# Inventory

## Description

Represents the distributed stock of a `PhysicalProduct` at a specific `Warehouse` (`DOMINIO 6`).

Inventory must always be linked to exactly one product and exactly one warehouse. Negative stock is never permitted under any circumstance.

## Attributes

| Attribute          | Type          | Description                                                        |
| ------------------ | ------------- | ---------------------------------------------------------------------- |
| product            | PhysicalProduct | Product to which this inventory record belongs.                  |
| warehouse          | Warehouse       | Warehouse where this stock is physically located.                |
| availableQuantity  | Integer         | Quantity currently available for reservation and sale.           |
| reservedQuantity   | Integer         | Quantity reserved for orders pending payment or dispatch.        |
| conditionStatus    | InventoryConditionStatus | Physical condition of the stock (e.g. Available, Damaged). |

## Relationships

* An `Inventory` record belongs to exactly one `PhysicalProduct` and exactly one `Warehouse`.
* An `Inventory` record generates zero or more `InventoryMovement` instances.

## Business Rules

```text
availableQuantity must never be negative.
Stock marked as DAMAGED cannot be reserved.
Inventory that does not exist cannot be reserved.
```

---

# InventoryMovement

## Description

Represents a discrete change applied to an `Inventory` record. Every change in stock must be captured through a movement, providing traceability of the inventory's history.

## Attributes

| Attribute      | Type                     | Description                                                    |
| -------------- | ------------------------ | ------------------------------------------------------------------ |
| identifier     | String                   | Unique identifier of the movement.                              |
| inventory      | Inventory                | Inventory record affected by the movement.                     |
| movementType   | InventoryMovementType    | Type of movement (Inbound, Reservation, Sale Outbound, Adjustment, Return Inbound). |
| quantity       | Integer                  | Quantity affected by the movement.                              |
| relatedOrder   | Order?                   | Order associated with the movement, when applicable.            |
| performedBy    | User                     | User who triggered the movement.                                |
| movementDate   | LocalDateTime            | Date and time the movement occurred.                            |

## Relationships

* An `InventoryMovement` affects exactly one `Inventory` record.
* An `InventoryMovement` may reference one `Order` when the movement is order-related (reservation, sale outbound, return inbound).

---

# ShoppingCart

## Description

Represents the buyer's provisional product selection prior to confirming an order (`OBJ-07`).

A cart is converted into an `Order` when the buyer confirms the purchase; the resulting order begins its lifecycle in the `CART` status described in the order state model.

## Attributes

| Attribute  | Type                | Description                                          |
| ---------- | ------------------- | ------------------------------------------------------- |
| buyer      | Buyer               | Buyer who owns the cart.                             |
| items      | List\<CartItem\>    | Provisional selection of products and quantities.    |
| lastUpdated| LocalDateTime       | Timestamp of the last modification to the cart.       |

## Relationships

* A `ShoppingCart` belongs to exactly one `Buyer`.
* A `ShoppingCart` is converted into exactly one `Order` upon checkout confirmation.

---

# CartItem

## Description

Represents a value object capturing a product and the quantity provisionally selected by the buyer.

## Type

Value Object (embedded in `ShoppingCart`)

## Attributes

| Attribute | Type    | Description                     |
| --------- | ------- | ---------------------------------- |
| product   | Product | Product selected by the buyer.  |
| quantity  | Integer | Quantity provisionally selected. |

---

# TrackableProcess (Abstract)

## Description

Represents any stateful commercial or logistics process executed within the marketplace that must remain traceable throughout its lifecycle.

Each significant transition of a trackable process must generate an `Operation`, which must subsequently be recorded in the `AuditLog`, supporting the traceability objective stated in the specification's introduction.

This class cannot be instantiated directly.

## Attributes

| Attribute  | Type   | Description                                     |
| ---------- | ------ | -------------------------------------------------- |
| identifier | String | Unique identifier of the trackable process.      |

## Relationships

* A `TrackableProcess` may generate zero or more `Operation` instances.

## Business Rule

```text
Every significant transition of a TrackableProcess
must generate an Operation.

Every Operation must be recorded in the AuditLog.
```

---

# Order

## Description

Represents the formal commercial commitment between a buyer and the marketplace. Its lifecycle is the central process of the system (`DOMINIO 7`).

An order that has reached the `DELIVERED` status can never be modified under any circumstance.

## Inherits From

`TrackableProcess`

## Attributes

| Attribute    | Type              | Description                                                    |
| ------------ | ----------------- | ------------------------------------------------------------------ |
| buyer        | Buyer             | Buyer who placed the order.                                     |
| items        | List\<OrderItem\> | Products, quantities, and prices included in the order.        |
| orderStatus  | OrderStatus       | Current stage of the order lifecycle.                           |
| creationDate | LocalDateTime     | Date and time the order was created from the cart.               |

## Relationships

* An `Order` is placed by exactly one `Buyer`.
* An `Order` contains one or more `OrderItem` values.
* An `Order` generates exactly one `Invoice` once paid.
* An `Order` generates one or more `Shipment` instances for its physical items.
* An `Order` may generate zero or more `Return` requests once delivered.

## Order Status Lifecycle

```text
CART -> PENDING_PAYMENT -> PAID -> DISPATCHED -> DELIVERED
```

## Examples of Generated Operations

* `ORDER_CREATED`
* `PAYMENT_CONFIRMED`
* `ORDER_DISPATCHED`
* `ORDER_DELIVERED`

## Business Rule

```text
An order with orderStatus = DELIVERED must not be modified.
```

---

# OrderItem

## Description

Represents a value object capturing a product, the quantity purchased, and the unit price agreed at the time of purchase.

## Type

Value Object (embedded in `Order`)

## Attributes

| Attribute  | Type       | Description                                     |
| ---------- | ---------- | -------------------------------------------------- |
| product    | Product    | Product included in the order.                  |
| quantity   | Integer    | Quantity purchased.                              |
| unitPrice  | BigDecimal | Unit price agreed at the time of purchase.       |

---

# Invoice

## Description

Represents the commercial billing information associated with a paid order (`DOMINIO` `Facturación`, `OBJ-09`).

## Attributes

| Attribute    | Type          | Description                                    |
| ------------ | ------------- | --------------------------------------------------- |
| identifier   | String        | Unique identifier of the invoice.               |
| order        | Order         | Order this invoice was generated from.          |
| buyer        | Buyer         | Buyer billed by this invoice.                   |
| issueDate    | LocalDateTime | Date and time the invoice was issued.            |
| totalAmount  | BigDecimal    | Total amount billed.                            |

## Relationships

* An `Invoice` is generated from exactly one `Order`.
* An `Invoice` bills exactly one `Buyer`.

---

# Shipment

## Description

Represents the logistics process required to package, dispatch, and transport an order's physical items to the buyer (`DOMINIO` `Envíos`, `OBJ-10`).

## Inherits From

`TrackableProcess`

## Attributes

| Attribute          | Type              | Description                                              |
| ------------------ | ----------------- | ------------------------------------------------------------ |
| order               | Order             | Order this shipment fulfills.                             |
| originWarehouse     | Warehouse         | Warehouse from which the items are dispatched.             |
| logisticsOperator   | LogisticsOperator | Operator responsible for the shipment.                     |
| destinationAddress  | String            | Delivery address for this shipment.                        |
| shipmentStatus      | ShipmentStatus    | Current status of the shipment.                            |
| dispatchDate        | LocalDateTime?    | Date and time the shipment left the warehouse.              |
| deliveryDate        | LocalDateTime?    | Date and time the shipment was confirmed delivered.         |

## Relationships

* A `Shipment` fulfills exactly one `Order`.
* A `Shipment` departs from exactly one `Warehouse`.
* A `Shipment` is handled by exactly one `LogisticsOperator`.

## Examples of Generated Operations

* `SHIPMENT_PREPARED`
* `SHIPMENT_DISPATCHED`
* `SHIPMENT_DELIVERED`

---

# Return

## Description

Represents a buyer's request to return one or more items from a delivered order (`DOMINIO` `Devoluciones`, `OBJ-11`).

## Inherits From

`TrackableProcess`

## Attributes

| Attribute     | Type          | Description                                          |
| ------------- | ------------- | --------------------------------------------------------- |
| order         | Order         | Order the return applies to.                          |
| items         | List\<OrderItem\> | Items being returned.                              |
| reason        | String        | Reason provided by the buyer for the return.           |
| returnStatus  | ReturnStatus  | Current status of the return request.                  |
| requestDate   | LocalDateTime | Date and time the return was requested.                 |

## Relationships

* A `Return` applies to exactly one `Order`.
* A `Return` may result in exactly one `Refund` when approved.

## Examples of Generated Operations

* `RETURN_REQUESTED`
* `RETURN_APPROVED`
* `RETURN_REJECTED`

---

# Refund

## Description

Represents the monetary reimbursement issued to a buyer as a result of an approved `Return` (`DOMINIO` `Reembolsos`, `OBJ-11`).

## Inherits From

`TrackableProcess`

## Attributes

| Attribute     | Type          | Description                                     |
| ------------- | ------------- | ---------------------------------------------------- |
| relatedReturn | Return        | Return that originated this refund.              |
| amount        | BigDecimal    | Amount to be reimbursed to the buyer.              |
| refundStatus  | RefundStatus  | Current status of the refund.                      |
| processedBy   | Administrator | Administrator who processed the refund.             |

## Relationships

* A `Refund` originates from exactly one `Return`.
* A `Refund` is processed by one `Administrator`.

## Examples of Generated Operations

* `REFUND_PROCESSED`
* `REFUND_REJECTED`

---

# Operation

## Description

Represents a significant business action executed over a `TrackableProcess`.

Operations provide traceability between users and the commercial or logistics processes they act upon.

An operation represents an event or action that occurred; it is distinct from the current status of the affected process.

For example:

```text
Order.orderStatus = DISPATCHED
```

represents the current state of the order, while:

```text
Operation.operationType = ORDER_DISPATCHED
```

represents the action that caused the state change.

## Attributes

| Attribute        | Type              | Description                                              |
| ----------------- | ----------------- | ------------------------------------------------------------ |
| operationId        | String            | Unique operation identifier.                              |
| operationType       | OperationType      | Category of the business operation.                        |
| executionDate       | LocalDateTime      | Date and time when the operation occurred.                  |
| performedBy         | User               | User responsible for executing the operation.               |
| affectedProcess     | TrackableProcess   | Process affected by the operation.                          |

## Relationships

* One `TrackableProcess` may generate zero or more `Operation` instances.
* Each `Operation` affects one `TrackableProcess`.
* Each `Operation` is performed by one `User`.
* Each significant `Operation` must be recorded in the `AuditLog`.

---

# AuditLog

## Description

Represents the immutable audit trail of the marketplace, supporting the traceability requirement stated in the specification's introduction and objective `OBJ-12`.

Audit records are append-only and must not be modified or deleted after being persisted.

## Attributes

| Attribute        | Type                | Description                                                  |
| ----------------- | ------------------- | ------------------------------------------------------------ |
| auditId            | String              | Unique identifier of the audit record.                       |
| operationType       | OperationType        | Type of business operation recorded.                          |
| operationDate       | LocalDateTime        | Timestamp when the event occurred.                            |
| performedBy         | User                 | User responsible for the operation.                           |
| userRole            | UserRole             | Role of the user at the time of execution.                    |
| affectedProcess     | TrackableProcess     | Process involved in the operation.                             |
| details             | Map<String, Object>  | Flexible document containing operation-specific information. |

## Business Rules

* Audit records are immutable.
* Audit records are append-only.
* An audit record cannot be deleted after persistence.
* Every significant business operation must produce an audit record.
* The `userRole` must represent the role applicable at the time the operation was performed.

---

# Domain Lifecycle Relationship

The general lifecycle of a trackable process is:

```text
TrackableProcess
      │
      │ significant business action
      ▼
  Operation
      │
      │ audit registration
      ▼
  AuditLog
```

For example, when an order is dispatched:

```text
Order
    │
    │ orderStatus changes
    ▼
DISPATCHED
    │
    ├── Operation
    │      operationType = ORDER_DISPATCHED
    │      performedBy = LogisticsOperator
    │
    └── AuditLog
           operationType = ORDER_DISPATCHED
           affectedProcess = Order
           performedBy = LogisticsOperator
           details = operation-specific information
```

---

# Domain Value Objects

## Introduction

Value Objects represent immutable concepts within the marketplace domain.

Unlike Entities, they do not have their own identity. They are defined by their values and encapsulate controlled business concepts.

Value Objects prevent primitive values and scattered string literals from being used throughout the domain.

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

All controlled business values that require a business code, display name, and description inherit from this abstraction.

## Attributes

| Attribute   | Type   | Description                                           |
| ----------- | ------ | ----------------------------------------------------- |
| code        | String | Unique business identifier.                           |
| name        | String | Human-readable name displayed within the application. |
| description | String | Business definition of the catalog value.             |

## Characteristics

* Immutable.
* Equality is based on catalog values.
* Catalog values must be controlled by the domain.
* Catalog values must not be represented by arbitrary strings throughout the application.

---

# UserRole

## Description

Represents the responsibilities and permissions assigned to a user within the marketplace (`RG-02`).

## Inherits From

`DomainCatalog`

## Allowed Values

| Code                | Name                | Description                                            |
| -------------------- | -------------------- | ---------------------------------------------------------- |
| BUYER                | Buyer                | Purchases products published on the marketplace.         |
| SELLER               | Seller               | Registers and administers products.                       |
| LOGISTICS_OPERATOR   | Logistics Operator   | Operates warehouses and dispatches orders.                |
| ADMINISTRATOR        | Administrator        | Administers sellers and warehouses.                       |
| SUPERVISOR           | Supervisor           | Consultation and operational oversight profile.           |

---

# UserStatus

## Description

Represents the operational status of a user within the platform.

## Inherits From

`DomainCatalog`

## Allowed Values

| Code     | Name     | Description                                            |
| -------- | -------- | ---------------------------------------------------------- |
| ACTIVE   | Active   | User can access and operate on the platform.             |
| INACTIVE | Inactive | User exists but cannot perform operations.                 |
| BLOCKED  | Blocked  | User access has been suspended.                            |

---

# BuyerCommercialStatus

## Description

Represents the buyer's condition for participating in commercial processes (`DOMINIO 2`).

## Inherits From

`DomainCatalog`

## Allowed Values

| Code        | Name        | Description                                        |
| ----------- | ----------- | ------------------------------------------------------ |
| ACTIVE      | Active      | Buyer can place orders normally.                     |
| RESTRICTED  | Restricted  | Buyer's ability to place orders is limited.           |
| BLOCKED     | Blocked     | Buyer cannot place orders.                             |

---

# WarehouseType

## Description

Represents the classification of a warehouse (`DOMINIO 4`).

## Inherits From

`DomainCatalog`

## Allowed Values

| Code        | Name        | Description                                |
| ----------- | ----------- | ----------------------------------------------- |
| MARKETPLACE | Marketplace | Warehouse owned and operated by the Marketplace. |
| SELLER      | Seller      | Warehouse owned and operated by a seller.        |

---

# WarehouseStatus

## Description

Represents the operational status of a warehouse.

## Inherits From

`DomainCatalog`

## Allowed Values

| Code     | Name     | Description                             |
| -------- | -------- | -------------------------------------------- |
| ACTIVE   | Active   | Warehouse is fully operational.           |
| INACTIVE | Inactive | Warehouse is temporarily not in use.       |

---

# ProductType

## Description

Represents the discriminator between physical and digital products (`DOMINIO 5`).

## Inherits From

`DomainCatalog`

## Allowed Values

| Code     | Name     | Description                                    |
| -------- | -------- | ---------------------------------------------------- |
| PHYSICAL | Physical | Product requiring inventory and physical dispatch. |
| DIGITAL  | Digital  | Product delivered immediately after payment.      |

---

# ProductStatus

## Description

Represents the publication status of a product in the catalog.

## Inherits From

`DomainCatalog`

## Allowed Values

| Code          | Name          | Description                              |
| ------------- | ------------- | --------------------------------------------- |
| PUBLISHED     | Published     | Product is visible in the public catalog.  |
| SUSPENDED     | Suspended     | Product is temporarily hidden from the catalog. |
| DISCONTINUED  | Discontinued  | Product has been permanently retired.       |

---

# InventoryMovementType

## Description

Represents the type of change applied to an inventory record (`DOMINIO 6`).

## Inherits From

`DomainCatalog`

## Allowed Values

| Code               | Name               | Description                                      |
| ------------------- | ------------------- | ------------------------------------------------------ |
| INBOUND              | Inbound             | Stock entering the warehouse.                        |
| RESERVATION          | Reservation         | Stock reserved for a pending order.                   |
| SALE_OUTBOUND        | Sale Outbound       | Stock leaving the warehouse due to a confirmed sale.  |
| ADJUSTMENT           | Adjustment          | Manual correction of stock levels.                     |
| RETURN_INBOUND       | Return Inbound      | Stock re-entering the warehouse from a return.          |

---

# InventoryConditionStatus

## Description

Represents the physical condition of a unit of stock.

## Inherits From

`DomainCatalog`

## Allowed Values

| Code      | Name      | Description                            |
| --------- | --------- | ------------------------------------------- |
| AVAILABLE | Available | Stock is in sellable condition.          |
| DAMAGED   | Damaged   | Stock is damaged and cannot be reserved. |

---

# OrderStatus

## Description

Represents the stage of the order lifecycle, as defined in `DOMINIO 7` of the specification.

## Inherits From

`DomainCatalog`

## Allowed Values

| Code            | Name             | Description                                              |
| ---------------- | ---------------- | ---------------------------------------------------------- |
| CART             | Cart             | Provisional product selection, not yet confirmed.        |
| PENDING_PAYMENT  | Pending Payment  | Order confirmed; awaiting financial confirmation.          |
| PAID             | Paid             | Payment confirmed; preparation process starts.              |
| DISPATCHED       | Dispatched       | Order has physically left the warehouse.                    |
| DELIVERED        | Delivered        | Order delivery has been confirmed; order is finalized.       |

---

# ShipmentStatus

## Description

Represents the current stage of a shipment's logistics process (`DOMINIO 10`).

## Inherits From

`DomainCatalog`

## Allowed Values

| Code             | Name             | Description                                    |
| ----------------- | ----------------- | ---------------------------------------------------- |
| IN_PREPARATION     | In Preparation     | Items are being packaged for dispatch.             |
| DISPATCHED          | Dispatched         | Shipment has left the origin warehouse.             |
| IN_TRANSIT          | In Transit         | Shipment is being transported to the buyer.          |
| DELIVERED           | Delivered          | Shipment has been delivered to the buyer.            |

---

# ReturnStatus

## Description

Represents the current stage of a return request (`DOMINIO 11`).

## Inherits From

`DomainCatalog`

## Allowed Values

| Code       | Name       | Description                            |
| ---------- | ---------- | ------------------------------------------- |
| REQUESTED  | Requested  | Return has been requested by the buyer.  |
| APPROVED   | Approved   | Return has been approved.                 |
| REJECTED   | Rejected   | Return has been denied.                   |
| COMPLETED  | Completed  | Returned items have been received and processed. |

---

# RefundStatus

## Description

Represents the current stage of a refund (`DOMINIO 11`).

## Inherits From

`DomainCatalog`

## Allowed Values

| Code      | Name      | Description                             |
| --------- | --------- | -------------------------------------------- |
| PENDING   | Pending   | Refund has been requested but not processed. |
| PROCESSED | Processed | Refund has been paid out to the buyer.        |
| REJECTED  | Rejected  | Refund request has been denied.                |

---

# OperationType

## Description

Represents the type of significant business operation executed within the marketplace.

Every significant operation generated by a trackable process must reference one operation type.

## Inherits From

`DomainCatalog`

## Allowed Values

| Code                | Name                | Description                                          |
| -------------------- | -------------------- | --------------------------------------------------------- |
| SELLER_REGISTERED     | Seller Registered     | Registration of a new seller.                            |
| WAREHOUSE_REGISTERED  | Warehouse Registered  | Registration of a new warehouse.                          |
| PRODUCT_PUBLISHED     | Product Published     | Publication of a product in the catalog.                  |
| INVENTORY_ADJUSTED    | Inventory Adjusted    | Manual correction of inventory levels.                     |
| ORDER_CREATED         | Order Created         | Confirmation of an order from a shopping cart.             |
| PAYMENT_CONFIRMED     | Payment Confirmed     | Confirmation of payment for an order.                       |
| ORDER_DISPATCHED      | Order Dispatched      | Physical dispatch of an order from the warehouse.            |
| ORDER_DELIVERED       | Order Delivered       | Confirmed delivery of an order to the buyer.                  |
| SHIPMENT_PREPARED     | Shipment Prepared     | Packaging of a shipment prior to dispatch.                    |
| SHIPMENT_DISPATCHED   | Shipment Dispatched   | Departure of a shipment from the origin warehouse.              |
| SHIPMENT_DELIVERED    | Shipment Delivered    | Confirmed delivery of a shipment.                                |
| RETURN_REQUESTED      | Return Requested      | Submission of a return request by the buyer.                     |
| RETURN_APPROVED       | Return Approved       | Approval of a return request.                                     |
| RETURN_REJECTED       | Return Rejected       | Rejection of a return request.                                     |
| REFUND_PROCESSED      | Refund Processed      | Successful payout of a refund.                                     |
| REFUND_REJECTED       | Refund Rejected       | Denial of a refund request.                                        |
| INVOICE_ISSUED        | Invoice Issued        | Issuance of an invoice for a paid order.                            |

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

---

# Domain Design Rules

## Users and Roles

* Every specialization of `User` (`Buyer`, `Seller`, `LogisticsOperator`, `Administrator`, `Supervisor`) inherits `identifier`, `fullName`, `email`, `role`, and `status` from `User`.
* `role` is defined in `User` and must not be duplicated by its specializations (`RG-02`).
* `RG-03`: no participant may administer information outside the scope defined by its role.

## Products

* `Product` is abstract; only `PhysicalProduct` and `DigitalProduct` may be instantiated.
* Only `PhysicalProduct` participates in `Inventory` and `InventoryMovement`.
* `DigitalProduct` is delivered immediately upon payment confirmation and never generates a `Shipment`.

## Inventory

* `Inventory` must always reference exactly one `Product` and exactly one `Warehouse`.
* `availableQuantity` must never become negative under any circumstance.
* Stock in `DAMAGED` condition cannot be reserved.
* Every change to an `Inventory` record must be captured by an `InventoryMovement`.

## Trackable Processes and Operations

* `Order`, `Shipment`, `Return`, and `Refund` specialize `TrackableProcess`.
* Every significant transition of a `TrackableProcess` must generate an `Operation`.
* An `Operation` references the affected `TrackableProcess`.
* An `Operation` references the `User` who performed the action.
* Process status represents the current state; operation represents an event or action that occurred.

## Orders

* An order finalized with `orderStatus = DELIVERED` must never be modified.
* A `ShoppingCart` is converted into an `Order` upon checkout confirmation; the resulting order starts in the `CART` status.

## Audit Trail

* Every significant operation must be recorded in the `AuditLog`.
* Audit records are immutable and append-only.
* Audit records must preserve the user role applicable when the operation was performed.
* Operation-specific information may be stored in the flexible `details` document.

## Value Objects

* Value Objects are immutable.
* Equality is determined by their values rather than object identity.
* Business entities reference Value Objects instead of primitive strings for controlled business concepts.
* Primitive enumerations are reserved for fixed technical concepts that do not require business metadata or behavior.

# Services

## Introduction

This document provides a conceptual overview of the services that compose the NexusMarket Marketplace Information Management System.

The services described here define the main business capabilities exposed by the system, directly traceable to the functional objectives (`OBJ-01` to `OBJ-12`) of the specification. At this level, each service is described only in terms of its purpose and responsibility within the domain.

The detailed definition of each service — including inputs, outputs, business rules, validations, authorization requirements, domain interactions, exceptions, persistence considerations, and technical implementation — will be documented in separate files organized by **subdomain**.

The service documentation is therefore divided conceptually into the following subdomains:

- **User Management** (`OBJ-01`)
- **Seller Management** (`OBJ-02`)
- **Buyer Management** (`OBJ-03`)
- **Warehouse Management** (`OBJ-04`)
- **Catalog Management** (`OBJ-05`)
- **Inventory Management** (`OBJ-06`)
- **Shopping Cart Management** (`OBJ-07`)
- **Order Management** (`OBJ-08`)
- **Invoicing Management** (`OBJ-09`)
- **Logistics Management** (`OBJ-10`)
- **Return and Refund Management** (`OBJ-11`)
- **Administrative Reporting** (`OBJ-12`)
- **Authorization**

---

# User Management Services

## Register User

Creates a new platform user with the identity and role information required for authentication and authorization (`OBJ-01`).

## Consult User

Retrieves the information of a platform user according to the access permissions of the requesting user.

## Update User

Updates the information maintained for an existing user according to the applicable business rules.

## Change User Status

Changes the operational status of a user, such as activating, deactivating, or blocking the user's platform access.

---

# Seller Management Services

## Register Seller

Creates a new seller and its first warehouse, incorporated by an `Administrator`. Sellers cannot self-register (`OBJ-02`, `DOMINIO 3`).

## Consult Seller

Retrieves the information of a seller according to the requesting user's permissions.

## Update Seller

Updates the information maintained for an existing seller.

## Change Seller Status

Changes the operational status of a seller.

## Consult Seller Products

Retrieves the products and warehouses associated with a seller.

---

# Buyer Management Services

## Register Buyer

Creates a new buyer, establishing the buyer's initial address and commercial status (`OBJ-03`).

## Consult Buyer

Retrieves the information of a buyer according to the requesting user's permissions. A buyer may never access another buyer's information (`DOMINIO 2`).

## Update Buyer

Updates the information maintained for an existing buyer, such as delivery addresses.

## Change Buyer Commercial Status

Changes the buyer's commercial status, such as restricting or blocking the buyer's ability to place orders.

## Consult Buyer Orders

Retrieves the orders placed by a buyer, loaded on demand.

---

# Warehouse Management Services

## Register Warehouse

Creates a new warehouse, classified as belonging to the Marketplace or to a Seller (`OBJ-04`, `DOMINIO 4`).

## Consult Warehouse

Retrieves the information of a warehouse according to the requesting user's permissions.

## Update Warehouse

Updates the information maintained for an existing warehouse.

## Change Warehouse Status

Changes the operational status of a warehouse.

---

# Catalog Management Services

## Register Product

Creates a new product (physical or digital) and associates it with the requesting seller (`OBJ-05`, `DOMINIO 5`).

## Consult Product

Retrieves the information of a product according to the requesting user's permissions.

## Update Product

Updates the information maintained for an existing product, including its variants.

## Publish Product

Changes a product's status to `PUBLISHED`, making it visible in the public catalog.

## Suspend Product

Changes a product's status to `SUSPENDED`, temporarily hiding it from the catalog.

## Discontinue Product

Changes a product's status to `DISCONTINUED`.

## Consult Public Catalog

Retrieves the products currently visible in the public catalog.

---

# Inventory Management Services

## Register Inventory

Creates the initial inventory record linking a physical product to a warehouse (`OBJ-06`, `DOMINIO 6`).

## Consult Inventory

Retrieves the current stock levels of a product across one or more warehouses.

## Register Inbound Movement

Registers an increase in stock and generates the corresponding `InventoryMovement`.

## Reserve Inventory

Reserves stock for a pending order, validating that the requested quantity is available and not marked as damaged.

## Register Sale Outbound Movement

Reduces stock following a confirmed sale and generates the corresponding `InventoryMovement`.

## Register Inventory Adjustment

Manually corrects stock levels and generates the corresponding `InventoryMovement`.

## Register Return Inbound Movement

Increases stock following an approved return and generates the corresponding `InventoryMovement`.

---

# Shopping Cart Management Services

## Add Item to Cart

Adds a product and quantity to the buyer's active shopping cart, creating the cart if it does not yet exist (`OBJ-07`).

## Update Cart Item

Updates the quantity of an item already present in the shopping cart.

## Remove Item from Cart

Removes a product from the shopping cart.

## Consult Cart

Retrieves the buyer's current shopping cart contents.

## Clear Cart

Empties the buyer's shopping cart.

---

# Order Management Services

## Confirm Order

Converts a buyer's shopping cart into an `Order`, starting the order lifecycle in the `CART` status (`OBJ-08`, `DOMINIO 7`).

## Confirm Payment

Validates payment for an order and transitions it to the `PAID` status.

## Consult Order

Retrieves the information of an order according to the requesting user's permissions.

## Dispatch Order

Transitions an order to the `DISPATCHED` status once its shipment has left the warehouse.

## Confirm Delivery

Transitions an order to the `DELIVERED` status, finalizing the order. A delivered order can no longer be modified.

---

# Invoicing Management Services

## Issue Invoice

Generates an invoice for a paid order, recording the billed amount and buyer information (`OBJ-09`).

## Consult Invoice

Retrieves an invoice according to the requesting user's permissions.

---

# Logistics Management Services

## Prepare Shipment

Creates a shipment for an order's physical items and assigns the responsible `LogisticsOperator` (`OBJ-10`, `DOMINIO` `Envíos`).

## Dispatch Shipment

Marks a shipment as dispatched from its origin warehouse.

## Confirm Shipment Delivery

Marks a shipment as delivered and triggers the corresponding order delivery confirmation.

## Consult Shipment

Retrieves the information of a shipment according to the requesting user's permissions.

---

# Return and Refund Management Services

## Request Return

Creates a return request for one or more items of a delivered order, initiating its lifecycle (`OBJ-11`, `DOMINIO` `Devoluciones`).

## Approve Return

Approves a return request after applying the required business validations.

## Reject Return

Rejects a return request and records the corresponding decision.

## Process Refund

Issues a refund associated with an approved return and records the corresponding business operation (`DOMINIO` `Reembolsos`).

## Reject Refund

Denies a refund request and records the corresponding decision.

## Consult Return

Retrieves the information of a return or refund according to the requesting user's permissions.

---

# Administrative Reporting Services

## Consult Operations

Retrieves the operations associated with trackable processes according to the requesting user's permissions (`OBJ-12`).

## Consult Audit Log

Retrieves historical audit records according to the access permissions of the requesting user.

## Generate Administrative Report

Consolidates operational information (orders, inventory, returns, sellers) for administrative consultation.

---

# Authorization Services

## Validate Permissions

Determines whether a user has permission to perform a specific business operation based on the user's role and status (`RG-02`, `RG-03`).

## Validate Buyer Access

Determines whether a user is authorized to access information belonging to a specific buyer.

## Validate Seller Access

Determines whether a user is authorized to access or operate on a specific seller's products or warehouses.

## Validate Process Access

Determines whether a user is authorized to access or operate on a specific `TrackableProcess` (`Order`, `Shipment`, `Return`, `Refund`).

---

# Service Organization

The services described in this document provide the **high-level service catalog** of the system.

They intentionally do not describe implementation details or complete business workflows.

Detailed specifications will be maintained in separate Markdown files organized by subdomain. For example:

```text
services/
│   └── user-services.md
│
│   └── seller-services.md
│
│   └── buyer-services.md
│
│   └── warehouse-services.md
│
│   └── catalog-services.md
│
│   └── inventory-services.md
│
│   └── shopping-cart-services.md
│
│   └── order-services.md
│
│   └── invoicing-services.md
│
│   └── logistics-services.md
│
│   └── return-refund-services.md
│
│   └── operation-audit-services.md
│
    └── authorization-services.md
```

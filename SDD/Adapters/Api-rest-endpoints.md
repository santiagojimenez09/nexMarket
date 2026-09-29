# API REST Endpoints Specification & Contracts

## 1. Overview

This document specifies the exact REST API contracts, HTTP methods, headers, request bodies, and responses for the **NexusMarket Marketplace Information Management System**, organized strictly by **System Roles** corresponding to the **Role Input Ports** (`domain/ports/in/`).

---

## 2. Standard Headers & Error Contract

- **Protected Endpoints:** Require `Authorization: Bearer <jwt_token>`
- **Content Types:** `Content-Type: application/json`, `Accept: application/json`
- **Request Tracing:** `X-Request-Id` is propagated on all responses.
- **Errors:** Standard error envelope defined in `SDD/Adapters/Global-exception-handler.md`.

---

## 3. Public Access Endpoints (`PublicAccessPort`)

### 3.1. User Login
- **Method:** `POST`
- **Path:** `/api/v1/public/auth/login`
- **Request Body (`LoginRequestDTO`):**
```json
{
  "email": "buyer@example.com",
  "password": "Password123!"
}
```
- **Response (`LoginResponseDTO` - HTTP 200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "expiresIn": 3600,
  "user": {
    "identifier": "USR-101",
    "email": "buyer@example.com",
    "fullName": "Juan Perez",
    "role": "BUYER"
  }
}
```

### 3.2. Buyer Self-Registration
- **Method:** `POST`
- **Path:** `/api/v1/public/buyers/register`
- **Request Body (`RegisterBuyerRequestDTO`):**
```json
{
  "identifier": "1017123456",
  "fullName": "Juan Perez",
  "email": "buyer@example.com",
  "password": "SecurePassword123!",
  "primaryAddress": "Calle 50 # 40 - 20"
}
```
- **Response (HTTP 201 Created)**

### 3.3. Consult Public Catalog
- **Method:** `GET`
- **Path:** `/api/v1/public/catalog`
- **Response (HTTP 200 OK):** List of published products (`ProductResponseDTO`).

---

## 4. Buyer Endpoints (`BuyerPort`) - Role `BUYER`

### 4.1. Consult Shopping Cart
- **Method:** `GET`
- **Path:** `/api/v1/buyers/cart`
- **Response (HTTP 200 OK):** `ShoppingCartResponseDTO`

### 4.2. Add Item to Cart
- **Method:** `POST`
- **Path:** `/api/v1/buyers/cart/items`
- **Request Body (`CartItemRequestDTO`):**
```json
{
  "productId": "PROD-101",
  "quantity": 2
}
```

### 4.3. Checkout (Confirm Order)
- **Method:** `POST`
- **Path:** `/api/v1/buyers/checkout`
- **Response (`OrderResponseDTO` - HTTP 201 Created):** Order created with status `PENDING_PAYMENT`.

### 4.4. Request Return
- **Method:** `POST`
- **Path:** `/api/v1/buyers/orders/{orderId}/returns`
- **Request Body (`RequestReturnDTO`):**
```json
{
  "reason": "Product arrived defective",
  "items": [
    { "productId": "PROD-101", "quantity": 1 }
  ]
}
```

---

## 5. Seller Endpoints (`SellerPort`) - Role `SELLER`

### 5.1. Register Product
- **Method:** `POST`
- **Path:** `/api/v1/sellers/products`
- **Request Body (`CreateProductRequestDTO`):**
```json
{
  "identifier": "SKU-9988",
  "name": "Mechanical Keyboard RGB",
  "description": "Custom mechanical keyboard",
  "productType": "PHYSICAL",
  "weight": 1.25,
  "variants": [
    { "variantName": "Switch", "variantValue": "Red", "skuSuffix": "-RED" }
  ]
}
```

### 5.2. Publish Product
- **Method:** `PATCH`
- **Path:** `/api/v1/sellers/products/{productId}/publish`
- **Response:** HTTP 200 OK (`ProductStatus = PUBLISHED`).

---

## 6. Logistics Operator Endpoints (`LogisticsOperatorPort`) - Role `LOGISTICS_OPERATOR`

### 6.1. Register Inbound Stock
- **Method:** `POST`
- **Path:** `/api/v1/logistics/inventory/inbound`
- **Request Body:**
```json
{
  "productId": "SKU-9988",
  "warehouseId": "WH-01",
  "quantity": 50
}
```

### 6.2. Prepare Shipment
- **Method:** `POST`
- **Path:** `/api/v1/logistics/shipments/prepare`
- **Request Body:**
```json
{
  "orderId": "ORD-5541",
  "originWarehouseId": "WH-01",
  "destinationAddress": "Calle 50 # 40 - 20"
}
```

### 6.3. Dispatch Shipment
- **Method:** `PATCH`
- **Path:** `/api/v1/logistics/shipments/{shipmentId}/dispatch`

---

## 7. Administrator Endpoints (`AdministratorPort`) - Role `ADMINISTRATOR`

### 7.1. Incorporate Seller
- **Method:** `POST`
- **Path:** `/api/v1/admin/sellers`
- **Request Body (`RegisterSellerRequestDTO`):**
```json
{
  "identifier": "NIT-900123456",
  "fullName": "Tech Store SAS",
  "email": "sales@techstore.com",
  "initialWarehouseName": "Bodega Principal Bogotá",
  "initialWarehouseAddress": "Zona Industrial Almagrario"
}
```

### 7.2. Approve / Reject Return
- **Method:** `PATCH`
- **Path:** `/api/v1/admin/returns/{returnId}/approve`

### 7.3. Process Refund
- **Method:** `POST`
- **Path:** `/api/v1/admin/returns/{returnId}/refund`
- **Request Body:**
```json
{
  "amount": 250.00
}
```

---

## 8. Supervisor Endpoints (`SupervisorPort`) - Role `SUPERVISOR`

### 8.1. Operational Metrics Summary
- **Method:** `GET`
- **Path:** `/api/v1/supervisor/reports/summary`

### 8.2. Consult Audit Trail
- **Method:** `GET`
- **Path:** `/api/v1/supervisor/audit-logs`

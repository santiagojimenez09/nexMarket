# Output Ports

## Introduction

Output Ports define the contracts through which the Domain communicates with external resources.

The Domain owns these interfaces. Domain Services must never depend directly on:

- MySQL
- MongoDB
- JPA
- Spring
- HTTP
- REST
- external APIs
- persistence repositories

When a Domain Service requires information or functionality outside the Domain, it must use the corresponding Output Port.

The implementations of these ports belong to the Adapter layer.

---

# Architectural Rule

The dependency flow must always be:

```text
Domain Service
      |
      v
Output Port
      |
      v
Output Adapter
      |
      v
External Resource
```

For example:

```text
OrderService
      |
      v
OrderRepositoryPort
      |
      v
OrderMySqlAdapter
      |
      v
MySQL
```

For auditing:

```text
OperationAuditService
      |
      v
AuditLogRepositoryPort
      |
      v
AuditLogMongoAdapter
      |
      v
MongoDB
```

---

# General Parameter Rule

Output Port methods must work with Domain Models.

They must not receive DTOs, persistence entities, or primitive identifiers when the corresponding Domain Model already exists.

Incorrect:

```java
Optional<Product> findById(String productId);
```

Correct:

```java
Optional<Product> findByIdentifier(Product product);
```

Incorrect:

```java
void updateStock(String inventoryId, Integer quantity);
```

Correct:

```java
void update(Inventory inventory);
```

This keeps the Domain independent from persistence and transport representations.

---

# Output Ports

## 1. UserRepositoryPort

### Responsibility

Provides the Domain with persistence and query capabilities for `User` and its specializations.

### Methods

```java
public interface UserRepositoryPort {

    User save(User user);

    Optional<User> findByIdentifier(User user);

    Optional<User> findByEmail(User user);

    boolean existsByIdentifier(User user);

    boolean existsByEmail(User user);

    List<User> findByRole(User user);

    void update(User user);
}
```

### Main Consumers

- User Management Services
- Authorization Services
- Operation and Audit Services

---

## 2. BuyerRepositoryPort

### Responsibility

Provides persistence and query capabilities specific to `Buyer`.

### Methods

```java
public interface BuyerRepositoryPort {

    Buyer save(Buyer buyer);

    Optional<Buyer> findByIdentifier(Buyer buyer);

    void update(Buyer buyer);
}
```

### Main Consumers

- Buyer Management Services
- Order Management Services
- Authorization Services

---

## 3. SellerRepositoryPort

### Responsibility

Provides persistence and query capabilities specific to `Seller`.

### Methods

```java
public interface SellerRepositoryPort {

    Seller save(Seller seller);

    Optional<Seller> findByIdentifier(Seller seller);

    List<Seller> findByRegisteredBy(Administrator administrator);

    void update(Seller seller);
}
```

### Main Consumers

- Seller Management Services
- Catalog Management Services
- Warehouse Management Services

---

## 4. WarehouseRepositoryPort

### Responsibility

Provides persistence and query capabilities for `Warehouse`.

### Methods

```java
public interface WarehouseRepositoryPort {

    Warehouse save(Warehouse warehouse);

    Optional<Warehouse> findByIdentifier(Warehouse warehouse);

    List<Warehouse> findBySeller(Seller seller);

    List<Warehouse> findByType(Warehouse warehouse);

    void update(Warehouse warehouse);
}
```

### Main Consumers

- Warehouse Management Services
- Inventory Management Services
- Logistics Management Services

---

## 5. ProductRepositoryPort

### Responsibility

Provides persistence and query capabilities for `Product` and its specializations (`PhysicalProduct`, `DigitalProduct`).

### Methods

```java
public interface ProductRepositoryPort {

    Product save(Product product);

    Optional<Product> findByIdentifier(Product product);

    List<Product> findBySeller(Seller seller);

    List<Product> findByStatus(Product product);

    List<Product> findPublishedCatalog();

    void update(Product product);
}
```

### Main Consumers

- Catalog Management Services
- Shopping Cart Management Services
- Order Management Services
- Inventory Management Services

---

## 6. InventoryRepositoryPort

### Responsibility

Provides persistence and query capabilities for `Inventory`.

### Methods

```java
public interface InventoryRepositoryPort {

    Inventory save(Inventory inventory);

    Optional<Inventory> findByProductAndWarehouse(Inventory inventory);

    List<Inventory> findByProduct(Product product);

    List<Inventory> findByWarehouse(Warehouse warehouse);

    void update(Inventory inventory);
}
```

### Main Consumers

- Inventory Management Services
- Order Management Services
- Catalog Management Services

---

## 7. InventoryMovementRepositoryPort

### Responsibility

Provides persistence for `InventoryMovement` records, giving traceability to every stock change.

### Methods

```java
public interface InventoryMovementRepositoryPort {

    InventoryMovement save(InventoryMovement movement);

    List<InventoryMovement> findByInventory(Inventory inventory);

    List<InventoryMovement> findByOrder(Order order);

    List<InventoryMovement> findByType(InventoryMovement movement);
}
```

### Main Consumers

- Inventory Management Services
- Administrative Reporting Services

---

## 8. ShoppingCartRepositoryPort

### Responsibility

Provides persistence and query capabilities for `ShoppingCart`.

### Methods

```java
public interface ShoppingCartRepositoryPort {

    ShoppingCart save(ShoppingCart cart);

    Optional<ShoppingCart> findByBuyer(Buyer buyer);

    void update(ShoppingCart cart);

    void delete(ShoppingCart cart);
}
```

### Main Consumers

- Shopping Cart Management Services
- Order Management Services

---

## 9. OrderRepositoryPort

### Responsibility

Provides persistence and query capabilities for `Order`.

### Methods

```java
public interface OrderRepositoryPort {

    Order save(Order order);

    Optional<Order> findByIdentifier(Order order);

    List<Order> findByBuyer(Buyer buyer);

    List<Order> findByStatus(Order order);

    void update(Order order);
}
```

### Main Consumers

- Order Management Services
- Invoicing Management Services
- Logistics Management Services
- Return and Refund Management Services
- Authorization Services

---

## 10. InvoiceRepositoryPort

### Responsibility

Provides persistence and query capabilities for `Invoice`.

### Methods

```java
public interface InvoiceRepositoryPort {

    Invoice save(Invoice invoice);

    Optional<Invoice> findByOrder(Order order);

    List<Invoice> findByBuyer(Buyer buyer);
}
```

### Main Consumers

- Invoicing Management Services

---

## 11. ShipmentRepositoryPort

### Responsibility

Provides persistence and query capabilities for `Shipment`.

### Methods

```java
public interface ShipmentRepositoryPort {

    Shipment save(Shipment shipment);

    Optional<Shipment> findByIdentifier(Shipment shipment);

    List<Shipment> findByOrder(Order order);

    List<Shipment> findByLogisticsOperator(LogisticsOperator operator);

    void update(Shipment shipment);
}
```

### Main Consumers

- Logistics Management Services
- Order Management Services
- Operation and Audit Services

---

## 12. ReturnRepositoryPort

### Responsibility

Provides persistence and query capabilities for `Return`.

### Methods

```java
public interface ReturnRepositoryPort {

    Return save(Return returnRequest);

    Optional<Return> findByIdentifier(Return returnRequest);

    List<Return> findByOrder(Order order);

    List<Return> findByStatus(Return returnRequest);

    void update(Return returnRequest);
}
```

### Main Consumers

- Return and Refund Management Services
- Inventory Management Services

---

## 13. RefundRepositoryPort

### Responsibility

Provides persistence and query capabilities for `Refund`.

### Methods

```java
public interface RefundRepositoryPort {

    Refund save(Refund refund);

    Optional<Refund> findByReturn(Return returnRequest);

    List<Refund> findByStatus(Refund refund);

    void update(Refund refund);
}
```

### Main Consumers

- Return and Refund Management Services

---

## 14. OperationRepositoryPort

### Responsibility

Provides persistence for business `Operation` records.

`Operation` represents the business action executed over a `TrackableProcess`.

### Methods

```java
public interface OperationRepositoryPort {

    Operation save(Operation operation);

    Optional<Operation> findById(Operation operation);

    List<Operation> findByUser(User user);

    List<Operation> findByProcess(TrackableProcess process);

    List<Operation> findByType(Operation operation);
}
```

### Main Consumers

- Administrative Reporting Services
- Domain Services that execute auditable business operations

---

## 15. AuditLogRepositoryPort

### Responsibility

Provides persistence for immutable audit records.

The implementation is expected to use MongoDB, while the Domain remains completely unaware of MongoDB.

### Methods

```java
public interface AuditLogRepositoryPort {

    AuditLog save(AuditLog auditLog);

    List<AuditLog> findByUser(User user);

    List<AuditLog> findByProcess(TrackableProcess process);

    List<AuditLog> findByOperationType(AuditLog auditLog);
}
```

### Main Consumers

- Administrative Reporting Services
- Domain Services that generate auditable events

---

## 16. PasswordServicePort

### Responsibility

Abstracts password hashing and password verification.

The Domain must not depend directly on BCrypt, Argon2, Spring Security, or another password implementation.

### Methods

```java
public interface PasswordServicePort {

    boolean matches(User user);

    String encrypt(User user);
}
```

### Main Consumers

- User Management Services

### Architectural Rule

The concrete implementation belongs outside the Domain.

```text
PasswordServicePort
        ^
        |
PasswordSecurityAdapter
        |
        v
Password Hashing Library
```

---

## 17. JwtServicePort

### Responsibility

Abstracts JWT generation from the Domain.

The Domain must not depend directly on JWT libraries or security frameworks.

### Methods

```java
public interface JwtServicePort {

    String generateToken(User user);
}
```

### Main Consumer

- User Management Services (authentication)

### JWT Rule

The JWT must not contain the user's password.

The token may contain claims such as:

```text
identifier
role
```

and other claims strictly required by the application.

---

## 18. NotificationPort

### Responsibility

Abstracts communication with external notification systems.

Possible channels include:

- Email
- SMS
- Push Notification

### Preferred Method

If a `Notification` Domain Model exists, prefer:

```java
public interface NotificationPort {

    void send(Notification notification);
}
```

This is preferable to exposing transport-specific parameters such as email addresses or message formats directly in the Domain.

### Main Consumers

- Order Management Services
- Logistics Management Services
- Return and Refund Management Services

---

## 19. AuthorizationPort

### Responsibility

Provides external authorization information only when the authorization decision cannot be made using information already contained in the Domain Models.

### Methods

```java
public interface AuthorizationPort {

    boolean isAuthorized(User user, Buyer buyer);

    boolean canOperateOn(User user, TrackableProcess process);

    boolean canApprove(User user, TrackableProcess process);
}
```

### Important Rule

This port must not replace normal Domain validation.

For example, if the authorization depends only on:

```java
user.getRole()
```

the Domain Service should evaluate that directly.

There is no reason to call `AuthorizationPort` simply to determine whether:

```text
ADMINISTRATOR
```

can approve a refund if that rule is already part of the Domain.

The port is reserved for information that must come from outside the Domain.

---

## 20. BusinessConfigurationPort

### Responsibility

Provides configurable business parameters that are external to the Domain entities.

Examples include:

- Return request window (days after delivery)
- Refund processing threshold requiring supervisor approval
- Other business parameters explicitly defined as configurable

### Methods

```java
public interface BusinessConfigurationPort {

    Integer getReturnWindowDays();

    BigDecimal getRefundApprovalThreshold();
}
```

### Main Consumers

- Return and Refund Management Services
- Authorization Services

### Example

Instead of hardcoding:

```java
if (daysSinceDelivery > 30) {
    ...
}
```

the Domain Service can use:

```text
ReturnService
      |
      v
BusinessConfigurationPort
      |
      v
Return Window Days
```

---

# Port Organization

The Domain package should contain:

```text
domain/
└── ports/
    ├── in/
    │
    └── out/
        +-- UserRepositoryPort
        |       ^
        |       |
        |   UserMySqlAdapter
        |
        +-- BuyerRepositoryPort
        |       ^
        |       |
        |   BuyerMySqlAdapter
        |
        +-- SellerRepositoryPort
        |       ^
        |       |
        |   SellerMySqlAdapter
        |
        +-- WarehouseRepositoryPort
        |       ^
        |       |
        |   WarehouseMySqlAdapter
        |
        +-- ProductRepositoryPort
        |       ^
        |       |
        |   ProductMySqlAdapter
        |
        +-- InventoryRepositoryPort
        |       ^
        |       |
        |   InventoryMySqlAdapter
        |
        +-- InventoryMovementRepositoryPort
        |       ^
        |       |
        |   InventoryMovementMySqlAdapter
        |
        +-- ShoppingCartRepositoryPort
        |       ^
        |       |
        |   ShoppingCartMySqlAdapter
        |
        +-- OrderRepositoryPort
        |       ^
        |       |
        |   OrderMySqlAdapter
        |
        +-- InvoiceRepositoryPort
        |       ^
        |       |
        |   InvoiceMySqlAdapter
        |
        +-- ShipmentRepositoryPort
        |       ^
        |       |
        |   ShipmentMySqlAdapter
        |
        +-- ReturnRepositoryPort
        |       ^
        |       |
        |   ReturnMySqlAdapter
        |
        +-- RefundRepositoryPort
        |       ^
        |       |
        |   RefundMySqlAdapter
        |
        +-- OperationRepositoryPort
        |       ^
        |       |
        |   OperationMySqlAdapter
        |
        +-- AuditLogRepositoryPort
        |       ^
        |       |
        |   AuditLogMongoAdapter
        |
        +-- PasswordServicePort
        |       ^
        |       |
        |   PasswordSecurityAdapter
        |
        +-- JwtServicePort
        |       ^
        |       |
        |   JwtSecurityAdapter
        |
        +-- NotificationPort
        |       ^
        |       |
        |   NotificationAdapter
        |
        +-- AuthorizationPort
        |       ^
        |       |
        |   AuthorizationAdapter
        |
        +-- BusinessConfigurationPort
                ^
                |
            ConfigurationAdapter
```

---

# Database Responsibility

A Port does not necessarily correspond one-to-one with a physical database table.

For example:

```text
ProductRepositoryPort
        |
        v
ProductMySqlAdapter
        |
        +-- product table
        +-- physical_product table
        +-- digital_product table
        +-- product_variant table
```

The Domain only knows:

```text
ProductRepositoryPort
```

It does not know how many tables are used to persist `Product`.

Therefore, the physical database design can evolve without changing the Domain.

---

# Service-to-Port Relationship

## User Management Services

Typically depend on:

```text
UserRepositoryPort
PasswordServicePort
JwtServicePort
AuthorizationPort
```

---

## Seller Management Services

Typically depend on:

```text
SellerRepositoryPort
UserRepositoryPort
WarehouseRepositoryPort
AuthorizationPort
OperationRepositoryPort
AuditLogRepositoryPort
```

---

## Buyer Management Services

Typically depend on:

```text
BuyerRepositoryPort
UserRepositoryPort
AuthorizationPort
NotificationPort
```

---

## Warehouse Management Services

Typically depend on:

```text
WarehouseRepositoryPort
SellerRepositoryPort
AuthorizationPort
OperationRepositoryPort
AuditLogRepositoryPort
```

---

## Catalog Management Services

Typically depend on:

```text
ProductRepositoryPort
SellerRepositoryPort
AuthorizationPort
OperationRepositoryPort
AuditLogRepositoryPort
```

---

## Inventory Management Services

Typically depend on:

```text
InventoryRepositoryPort
InventoryMovementRepositoryPort
ProductRepositoryPort
WarehouseRepositoryPort
AuthorizationPort
OperationRepositoryPort
AuditLogRepositoryPort
```

---

## Shopping Cart Management Services

Typically depend on:

```text
ShoppingCartRepositoryPort
ProductRepositoryPort
BuyerRepositoryPort
```

---

## Order Management Services

Typically depend on:

```text
OrderRepositoryPort
ShoppingCartRepositoryPort
InventoryRepositoryPort
BuyerRepositoryPort
AuthorizationPort
OperationRepositoryPort
AuditLogRepositoryPort
NotificationPort
```

---

## Invoicing Management Services

Typically depend on:

```text
InvoiceRepositoryPort
OrderRepositoryPort
OperationRepositoryPort
AuditLogRepositoryPort
```

---

## Logistics Management Services

Typically depend on:

```text
ShipmentRepositoryPort
OrderRepositoryPort
WarehouseRepositoryPort
AuthorizationPort
OperationRepositoryPort
AuditLogRepositoryPort
NotificationPort
```

---

## Return and Refund Management Services

Typically depend on:

```text
ReturnRepositoryPort
RefundRepositoryPort
OrderRepositoryPort
InventoryRepositoryPort
InventoryMovementRepositoryPort
AuthorizationPort
BusinessConfigurationPort
OperationRepositoryPort
AuditLogRepositoryPort
NotificationPort
```

---

## Authorization Services

May depend on:

```text
AuthorizationPort
UserRepositoryPort
BuyerRepositoryPort
SellerRepositoryPort
OrderRepositoryPort
```

Only use the repository ports when authorization requires information not already available in the supplied Domain Models.

---

## Administrative Reporting Services

Depend on:

```text
OperationRepositoryPort
AuditLogRepositoryPort
```

---

# Final Architectural Rules

The following rules are mandatory:

1. All Output Ports belong to the Domain.
2. Output Ports are interfaces.
3. Adapters implement Output Ports.
4. Domain Services never access repositories directly.
5. Domain Services never access databases directly.
6. Domain Services never access HTTP or REST directly.
7. Domain Services never depend on Spring, JPA, MongoDB, or MySQL.
8. Ports must use Domain Models rather than DTOs.
9. Do not represent Domain relationships using primitive IDs when a Domain Model can represent the relationship.
10. Use Output Ports only when the required information or capability is external to the Domain.
11. Do not create a Port merely because a physical database contains another table.
12. Business rules that can be evaluated from Domain Models must remain inside the Domain.
13. External/configurable business information must be accessed through an appropriate Output Port.
14. Persistence adapters translate between Domain Models and persistence representations.
15. The Domain must remain fully testable without a database or external infrastructure.

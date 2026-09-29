# Input Ports (Role-Based Use Case Interfaces)

## 1. Overview

Input Ports define the application entry contracts through which external delivery mechanisms (such as REST Controllers) interact with the core marketplace domain.

Following the course reference architecture (**DDD + Hexagonal Architecture**), **Input Ports are organized strictly by System Roles (`UserRole`)**, plus a dedicated public access port for unauthenticated operations. This ensures that:
- Every platform role exposes a dedicated, cohesive interface containing only the business operations permitted for that role (`RG-02`, `RG-03`).
- REST Controllers and Spring Security filters evaluate role permissions directly against the targeted Role Input Port.
- Methods in Input Ports operate exclusively on **Domain Models** and **Value Objects**, receiving the `User` domain model (reconstructed from JWT claims) and domain entities as parameters.

---

## 2. General Principles

### 2.1 Role Isolation
Each system role interacts with the application through its corresponding Role Input Port interface in `domain/ports/in/`:
- `PublicAccessPort` (Public / Unauthenticated)
- `BuyerPort` (`BUYER`)
- `SellerPort` (`SELLER`)
- `LogisticsOperatorPort` (`LOGISTICS_OPERATOR`)
- `AdministratorPort` (`ADMINISTRATOR`)
- `SupervisorPort` (`SUPERVISOR`)

### 2.2 Domain Model Parameters
All Input Port methods receive **Domain Models** (`User`, `Buyer`, `Seller`, `Product`, `Order`, `Inventory`, `Shipment`, `Return`, `Refund`) rather than primitive IDs or DTOs.
The `User` domain model passed to each service contains the identity and role reconstructed from the authenticated JWT token.

---

## 3. Input Port Definitions

### 3.1 PublicAccessPort
Exposes public operations that do not require prior authentication (login, registration of buyers, public catalog browsing):

```java
package application.domain.ports.in;

import application.domain.models.Buyer;
import application.domain.models.Product;
import application.domain.models.User;
import java.util.List;

public interface PublicAccessPort {
    String login(User user, String rawPassword);
    Buyer registerBuyer(Buyer buyer, String rawPassword);
    List<Product> consultPublicCatalog();
    Product consultProductDetail(Product product);
}
```

---

### 3.2 BuyerPort (`BUYER`)
Exposes commercial operations for buyers over their profile, cart, orders, and returns (`DOMINIO 2`, `DOMINIO 7`, `OBJ-03`, `OBJ-07`, `OBJ-08`, `OBJ-11`):

```java
package application.domain.ports.in;

import application.domain.models.*;
import java.util.List;

public interface BuyerPort {
    Buyer consultMyProfile(User user);
    void updateMyAddresses(User user, String primaryAddress, List<String> additionalAddresses);
    
    // Shopping Cart
    ShoppingCart consultMyCart(User user);
    ShoppingCart addItemToCart(User user, Product product, int quantity);
    ShoppingCart updateCartItem(User user, Product product, int quantity);
    ShoppingCart removeItemFromCart(User user, Product product);
    void clearCart(User user);
    
    // Orders
    Order checkoutCart(User user);
    Order consultOrder(User user, Order order);
    List<Order> consultMyOrders(User user);
    
    // Invoices
    Invoice consultMyInvoice(User user, Order order);
    
    // Returns
    Return requestReturn(User user, Order order, List<OrderItem> items, String reason);
    Return consultReturn(User user, Return returnRequest);
}
```

---

### 3.3 SellerPort (`SELLER`)
Exposes catalog, product variant, and inventory management for registered sellers (`DOMINIO 3`, `DOMINIO 5`, `OBJ-02`, `OBJ-05`):

```java
package application.domain.ports.in;

import application.domain.models.*;
import java.util.List;

public interface SellerPort {
    Seller consultMyProfile(User user);
    
    // Products & Catalog
    Product registerProduct(User user, Product product);
    Product updateProduct(User user, Product product);
    void publishProduct(User user, Product product);
    void suspendProduct(User user, Product product);
    void discontinueProduct(User user, Product product);
    List<Product> consultMyProducts(User user);
    
    // Warehouses & Stock
    List<Warehouse> consultMyWarehouses(User user);
    List<Inventory> consultMyInventory(User user, Warehouse warehouse);
}
```

---

### 3.4 LogisticsOperatorPort (`LOGISTICS_OPERATOR`)
Exposes operational capabilities for physical warehouse management, stock movements, packaging, and dispatching orders (`DOMINIO 4`, `DOMINIO 6`, `DOMINIO Envíos`, `OBJ-04`, `OBJ-06`, `OBJ-10`):

```java
package application.domain.ports.in;

import application.domain.models.*;
import java.util.List;

public interface LogisticsOperatorPort {
    List<Warehouse> consultOperatedWarehouses(User user);
    
    // Inventory Operations
    Inventory consultWarehouseInventory(User user, Inventory query);
    InventoryMovement registerInboundStock(User user, Inventory inventory, int quantity);
    InventoryMovement registerInventoryAdjustment(User user, Inventory inventory, int deltaQuantity);
    InventoryMovement registerReturnInbound(User user, Inventory inventory, int quantity, Order order);
    
    // Shipment & Dispatching
    Shipment prepareShipment(User user, Order order, Warehouse warehouse, String destinationAddress);
    Shipment dispatchShipment(User user, Shipment shipment);
    Shipment confirmDelivery(User user, Shipment shipment);
    List<Shipment> consultAssignedShipments(User user);
}
```

---

### 3.5 AdministratorPort (`ADMINISTRATOR`)
Exposes platform-level management for sellers, warehouses, refunds, users, and global policy administration (`DOMINIO 3`, `DOMINIO 4`, `DOMINIO Reembolsos`, `OBJ-01`, `OBJ-02`, `OBJ-04`, `OBJ-11`):

```java
package application.domain.ports.in;

import application.domain.models.*;
import application.domain.valueObjects.BuyerCommercialStatus;
import application.domain.valueObjects.UserStatus;
import application.domain.valueObjects.WarehouseStatus;

import java.math.BigDecimal;
import java.util.List;

public interface AdministratorPort {
    // User Administration
    User registerPlatformUser(User admin, User newUser, String rawPassword);
    void changeUserStatus(User admin, User user, UserStatus newStatus);
    void changeBuyerCommercialStatus(User admin, Buyer buyer, BuyerCommercialStatus newStatus);
    
    // Seller & Warehouse Incorporation
    Seller registerSeller(User admin, Seller seller, String initialWarehouseName, String initialWarehouseAddress);
    void changeSellerStatus(User admin, Seller seller, UserStatus newStatus);
    Warehouse registerWarehouse(User admin, Warehouse warehouse);
    void changeWarehouseStatus(User admin, Warehouse warehouse, WarehouseStatus newStatus);
    
    // Returns & Refunds
    Return approveReturn(User admin, Return returnRequest);
    Return rejectReturn(User admin, Return returnRequest, String reason);
    Refund processRefund(User admin, Return returnRequest, BigDecimal amount);
    Refund rejectRefund(User admin, Refund refund, String reason);
    
    // Consultations
    List<Seller> listSellers(User admin);
    List<Warehouse> listWarehouses(User admin);
}
```

---

### 3.6 SupervisorPort (`SUPERVISOR`)
Exposes read-only operational oversight, audit trails, and administrative metric reports (`OBJ-12`):

```java
package application.domain.ports.in;

import application.domain.models.*;
import java.util.List;
import java.util.Map;

public interface SupervisorPort {
    Map<String, Object> generateOperationalSummaryReport(User supervisor);
    List<AuditLog> consultAuditTrailByProcess(User supervisor, TrackableProcess process);
    List<AuditLog> consultAuditTrailByUser(User supervisor, User targetUser);
    List<Operation> consultOperations(User supervisor, Operation query);
    List<Order> consultAllOrders(User supervisor);
}
```

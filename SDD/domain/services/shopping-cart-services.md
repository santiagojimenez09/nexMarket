# Shopping Cart Services Specification

## 1. Overview
Defines domain services for provisional product selection (`OBJ-07`).

## 2. Services
- `AddItemToCartService`: Adds an item to the buyer's cart, creating the cart if not exists.
- `UpdateCartItemService`: Modifies the quantity of an item in the cart.
- `RemoveItemFromCartService`: Removes a product from the cart.
- `ConsultCartService`: Retrieves the active cart contents.
- `ClearCartService`: Empties the cart.

## 3. Invariants & Business Rules
- Quantity must always be strictly greater than zero.
- Output Ports used: `ShoppingCartRepositoryPort`, `ProductRepositoryPort`, `BuyerRepositoryPort`.

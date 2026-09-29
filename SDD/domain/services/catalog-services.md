# Catalog Services Specification

## 1. Overview
Defines domain services for product catalog registration and visibility (`DOMINIO 5`, `OBJ-05`).

## 2. Services
- `RegisterProductService`: Registers a `PhysicalProduct` (weight required) or `DigitalProduct` (download asset required) under a seller.
- `ConsultProductService`: Retrieves detailed product information with its variants.
- `PublishProductService`: Transitions product to `PUBLISHED` making it visible in public catalog.
- `SuspendProductService`: Temporarily hides product from public catalog (`SUSPENDED`).
- `DiscontinueProductService`: Permanently retires product (`DISCONTINUED`).
- `ConsultPublicCatalogService`: Retrieves only products in `PUBLISHED` status.

## 3. Invariants & Business Rules
- Only `PUBLISHED` products are visible to public buyers.
- Output Ports used: `ProductRepositoryPort`, `SellerRepositoryPort`.

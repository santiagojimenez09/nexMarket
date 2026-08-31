package application.domains;

import application.valueObjects.ProductStatus;
import application.valueObjects.ProductType;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class Product {
    private String identifier;
    private String name;
    private String description;
    private Seller seller;
    private List<ProductVariant> variants = new ArrayList<>();
    private ProductType productType;
    private ProductStatus status;
}

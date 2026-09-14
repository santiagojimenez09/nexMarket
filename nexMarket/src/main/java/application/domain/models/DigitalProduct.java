package application.domain.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DigitalProduct extends Product {
    private String deliveryAsset;
}

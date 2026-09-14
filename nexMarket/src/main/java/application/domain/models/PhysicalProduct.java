package application.domain.models;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PhysicalProduct extends Product {
    private BigDecimal weight;
}

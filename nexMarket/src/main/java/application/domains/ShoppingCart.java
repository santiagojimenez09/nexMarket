package application.domains;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ShoppingCart {
    private Buyer buyer;
    private List<CartItem> items = new ArrayList<>();
    private LocalDateTime lastUpdated;
}

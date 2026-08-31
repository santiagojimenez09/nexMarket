package application.domains;

import application.valueObjects.OrderStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Order extends TrackableProcess {
    private Buyer buyer;
    private List<OrderItem> items = new ArrayList<>();
    private OrderStatus orderStatus;
    private LocalDateTime creationDate;
}

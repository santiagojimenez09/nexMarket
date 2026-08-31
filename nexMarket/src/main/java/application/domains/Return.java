package application.domains;

import application.valueObjects.ReturnStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Return extends TrackableProcess {
    private Order order;
    private List<OrderItem> items = new ArrayList<>();
    private String reason;
    private ReturnStatus returnStatus;
    private LocalDateTime requestDate;
}

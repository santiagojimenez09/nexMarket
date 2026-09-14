package application.domain.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Invoice {
    private String identifier;
    private Order order;
    private Buyer buyer;
    private LocalDateTime issueDate;
    private BigDecimal totalAmount;
}

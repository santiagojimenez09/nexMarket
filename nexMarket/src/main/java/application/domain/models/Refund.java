package application.domain.models;

import application.domain.valueObjects.RefundStatus;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Refund extends TrackableProcess {
    private Return relatedReturn;
    private BigDecimal amount;
    private RefundStatus refundStatus;
    private Administrator processedBy;
}

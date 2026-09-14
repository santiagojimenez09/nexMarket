package application.domain.ports.in;

import application.domain.models.Administrator;
import application.domain.models.Refund;
import application.domain.models.Return;

import java.math.BigDecimal;

public interface ProcessRefundUseCase {
    Refund processRefund(Return returnRequest, BigDecimal amount, Administrator administrator);
}

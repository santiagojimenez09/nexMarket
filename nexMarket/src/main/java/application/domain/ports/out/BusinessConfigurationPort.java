package application.domain.ports.out;

import java.math.BigDecimal;

public interface BusinessConfigurationPort {
    Integer getReturnWindowDays();
    BigDecimal getRefundApprovalThreshold();
}

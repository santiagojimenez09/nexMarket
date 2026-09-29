package application.infrastructure.config;

import application.domain.ports.out.BusinessConfigurationPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DefaultBusinessConfigurationAdapter implements BusinessConfigurationPort {

    private final Integer returnWindowDays;
    private final BigDecimal refundApprovalThreshold;

    public DefaultBusinessConfigurationAdapter(
            @Value("${nexmarket.returns.window-days:30}") Integer returnWindowDays,
            @Value("${nexmarket.refunds.threshold:1000.00}") BigDecimal refundApprovalThreshold) {
        this.returnWindowDays = returnWindowDays;
        this.refundApprovalThreshold = refundApprovalThreshold;
    }

    @Override
    public Integer getReturnWindowDays() {
        return returnWindowDays;
    }

    @Override
    public BigDecimal getRefundApprovalThreshold() {
        return refundApprovalThreshold;
    }
}

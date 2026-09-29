package application.adapters.rest.mappers;

import application.adapters.rest.dtos.responses.RefundResponseDTO;
import application.adapters.rest.dtos.responses.ReturnResponseDTO;
import application.domain.models.Refund;
import application.domain.models.Return;

public class ReturnRestMapper {

    public static ReturnResponseDTO toResponseDTO(Return returnRequest) {
        if (returnRequest == null) return null;
        return ReturnResponseDTO.builder()
                .identifier(returnRequest.getIdentifier())
                .orderIdentifier(returnRequest.getOrder() != null ? returnRequest.getOrder().getIdentifier() : null)
                .returnStatus(returnRequest.getReturnStatus() != null ? returnRequest.getReturnStatus().name() : null)
                .reason(returnRequest.getReason())
                .requestDate(returnRequest.getRequestDate())
                .build();
    }

    public static RefundResponseDTO toRefundResponseDTO(Refund refund) {
        if (refund == null) return null;
        return RefundResponseDTO.builder()
                .identifier(refund.getIdentifier())
                .returnIdentifier(refund.getRelatedReturn() != null ? refund.getRelatedReturn().getIdentifier() : null)
                .amount(refund.getAmount())
                .refundStatus(refund.getRefundStatus() != null ? refund.getRefundStatus().name() : null)
                .build();
    }
}

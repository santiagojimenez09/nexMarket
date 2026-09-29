package application.adapters.rest.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestReturnRequestDTO {
    private String orderIdentifier;
    private String reason;
    private List<ReturnItemRequestDTO> items;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReturnItemRequestDTO {
        private String productIdentifier;
        private int quantity;
    }
}

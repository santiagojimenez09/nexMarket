package application.adapters.rest.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReturnResponseDTO {
    private String identifier;
    private String orderIdentifier;
    private String returnStatus;
    private String reason;
    private LocalDateTime requestDate;
}

package application.adapters.rest.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuyerResponseDTO {
    private String identifier;
    private String name;
    private String email;
    private String role;
    private String status;
    private String commercialStatus;
    private String primaryAddress;
    private List<String> additionalAddresses;
}

package application.adapters.rest.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerResponseDTO {
    private String identifier;
    private String name;
    private String email;
    private String companyName;
    private String taxIdentifier;
    private String contactPhone;
    private String status;
}

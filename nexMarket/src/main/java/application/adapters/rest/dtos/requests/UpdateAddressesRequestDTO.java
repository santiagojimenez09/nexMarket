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
public class UpdateAddressesRequestDTO {
    private String primaryAddress;
    private List<String> additionalAddresses;
}

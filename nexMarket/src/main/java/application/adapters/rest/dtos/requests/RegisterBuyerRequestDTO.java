package application.adapters.rest.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterBuyerRequestDTO {
    private String identifier;
    private String name;
    private String email;
    private String password;
    private String primaryAddress;
}

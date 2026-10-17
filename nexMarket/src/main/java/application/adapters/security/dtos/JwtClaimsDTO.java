package application.adapters.security.dtos;

import lombok.Data;

/**
 * DTO con el snapshot de User que viaja como claims firmados dentro del JWT.
 * Contrato del adaptador de seguridad: el dominio nunca ve claims ni mapeos JWT.
 */
@Data
public class JwtClaimsDTO {
    private Integer version;
    private String identifier;
    private String fullName;
    private String email;
    private String role;
    private String status;
    private String primaryAddress;
    private String commercialStatus;
}

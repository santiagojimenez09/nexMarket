package application.adapters.security.mappers;

import application.adapters.security.dtos.JwtClaimsDTO;
import application.domain.models.*;
import application.domain.valueObjects.BuyerCommercialStatus;
import application.domain.valueObjects.UserRole;
import application.domain.valueObjects.UserStatus;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Mapeo bidireccional entre el User del dominio y los claims JWT.
 * Toda la lógica de conversión entre dominio y claims vive aquí.
 */
public final class JwtClaimsMapper {

    private JwtClaimsMapper() {
    }

    public static JwtClaimsDTO toDto(User user) {
        if (user == null) return null;
        JwtClaimsDTO dto = new JwtClaimsDTO();
        dto.setVersion(user.getAuthTokenVersion() != null ? user.getAuthTokenVersion() : 1);
        dto.setIdentifier(user.getIdentifier());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole() != null ? user.getRole().name() : null);
        dto.setStatus(user.getStatus() != null ? user.getStatus().name() : null);

        if (user instanceof Buyer buyer) {
            dto.setPrimaryAddress(buyer.getPrimaryAddress());
            if (buyer.getCommercialStatus() != null) {
                dto.setCommercialStatus(buyer.getCommercialStatus().name());
            }
        }
        return dto;
    }

    public static Map<String, Object> toClaimsMap(User user) {
        return toClaimsMap(toDto(user));
    }

    public static Map<String, Object> toClaimsMap(JwtClaimsDTO dto) {
        Map<String, Object> claims = new LinkedHashMap<>();
        if (dto == null) return claims;
        claims.put("ver", dto.getVersion());
        claims.put("identifier", dto.getIdentifier());
        claims.put("fullName", dto.getFullName());
        claims.put("email", dto.getEmail());
        claims.put("role", dto.getRole());
        claims.put("status", dto.getStatus());
        if (dto.getPrimaryAddress() != null) {
            claims.put("primaryAddress", dto.getPrimaryAddress());
        }
        if (dto.getCommercialStatus() != null) {
            claims.put("commercialStatus", dto.getCommercialStatus());
        }
        return claims;
    }

    public static void applyClaims(JwtBuilder builder, User user) {
        toClaimsMap(user).forEach(builder::claim);
    }

    public static JwtClaimsDTO toDto(Claims claims) {
        if (claims == null) return null;
        JwtClaimsDTO dto = new JwtClaimsDTO();
        Object ver = claims.get("ver");
        if (ver instanceof Number number) {
            dto.setVersion(number.intValue());
        } else if (ver instanceof String text) {
            try {
                dto.setVersion(Integer.valueOf(text));
            } catch (NumberFormatException ignored) {
                dto.setVersion(1);
            }
        }
        dto.setIdentifier(claims.get("identifier", String.class));
        if (dto.getIdentifier() == null) {
            dto.setIdentifier(claims.getSubject());
        }
        dto.setFullName(claims.get("fullName", String.class));
        dto.setEmail(claims.get("email", String.class));
        dto.setRole(claims.get("role", String.class));
        dto.setStatus(claims.get("status", String.class));
        dto.setPrimaryAddress(claims.get("primaryAddress", String.class));
        dto.setCommercialStatus(claims.get("commercialStatus", String.class));
        return dto;
    }

    public static User toDomain(Claims claims) {
        return toDomain(toDto(claims));
    }

    public static User toDomain(JwtClaimsDTO dto) {
        if (dto == null) return null;
        UserRole role = parseUserRole(dto.getRole());
        User user = createUserForRole(role);
        user.setIdentifier(dto.getIdentifier());
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setRole(role);
        user.setStatus(parseUserStatus(dto.getStatus()));
        user.setAuthTokenVersion(dto.getVersion() != null ? dto.getVersion() : 1);

        if (user instanceof Buyer buyer) {
            buyer.setPrimaryAddress(dto.getPrimaryAddress());
            if (dto.getCommercialStatus() != null) {
                try {
                    buyer.setCommercialStatus(BuyerCommercialStatus.valueOf(dto.getCommercialStatus()));
                } catch (IllegalArgumentException ignored) {
                }
            }
        }
        return user;
    }

    private static User createUserForRole(UserRole role) {
        if (role == null) return new Buyer();
        return switch (role) {
            case BUYER -> new Buyer();
            case SELLER -> new Seller();
            case LOGISTICS_OPERATOR -> new LogisticsOperator();
            case ADMINISTRATOR -> new Administrator();
            case SUPERVISOR -> new Supervisor();
        };
    }

    public static String extractUserIdentifier(Claims claims) {
        if (claims == null) return null;
        String id = claims.get("identifier", String.class);
        return id != null ? id : claims.getSubject();
    }

    public static Integer extractTokenVersion(Claims claims) {
        if (claims == null) return null;
        Object ver = claims.get("ver");
        if (ver instanceof Number number) {
            return number.intValue();
        }
        if (ver instanceof String text) {
            try {
                return Integer.valueOf(text);
            } catch (NumberFormatException ignored) {
                return null;
            }
        }
        return null;
    }

    private static UserRole parseUserRole(String roleStr) {
        if (roleStr == null) return null;
        try {
            return UserRole.valueOf(roleStr);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private static UserStatus parseUserStatus(String statusStr) {
        if (statusStr == null) return UserStatus.ACTIVE;
        try {
            return UserStatus.valueOf(statusStr);
        } catch (IllegalArgumentException e) {
            return UserStatus.ACTIVE;
        }
    }
}

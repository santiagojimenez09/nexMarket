package application.adapters.persistence.jpa.mappers;

import application.adapters.persistence.jpa.entities.UserJpaEntity;
import application.domain.models.*;
import application.domain.valueObjects.UserRole;
import application.domain.valueObjects.UserStatus;

public class UserJpaMapper {

    public static UserJpaEntity toEntity(User domain) {
        if (domain == null) return null;
        return UserJpaEntity.builder()
                .identifier(domain.getIdentifier())
                .name(domain.getFullName())
                .email(domain.getEmail())
                .password(domain.getPassword())
                .role(domain.getRole() != null ? domain.getRole().name() : null)
                .status(domain.getStatus() != null ? domain.getStatus().name() : null)
                .authTokenVersion(domain.getAuthTokenVersion() != null ? domain.getAuthTokenVersion() : 1)
                .build();
    }

    public static User toDomain(UserJpaEntity entity) {
        if (entity == null) return null;
        UserRole role = null;
        if (entity.getRole() != null) {
            try {
                role = UserRole.valueOf(entity.getRole());
            } catch (IllegalArgumentException ignored) {
            }
        }

        User user = createUserForRole(role);
        user.setIdentifier(entity.getIdentifier());
        user.setFullName(entity.getName());
        user.setEmail(entity.getEmail());
        user.setPassword(entity.getPassword());
        user.setRole(role);
        if (entity.getStatus() != null) {
            try {
                user.setStatus(UserStatus.valueOf(entity.getStatus()));
            } catch (IllegalArgumentException ignored) {
            }
        }
        user.setAuthTokenVersion(entity.getAuthTokenVersion() != null ? entity.getAuthTokenVersion() : 1);
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
}

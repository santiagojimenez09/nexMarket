package application.adapters.persistence.jpa.mappers;

import application.adapters.persistence.jpa.entities.UserJpaEntity;
import application.domain.models.Buyer;
import application.domain.models.User;
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
                .build();
    }

    public static User toDomain(UserJpaEntity entity) {
        if (entity == null) return null;
        Buyer user = new Buyer();
        user.setIdentifier(entity.getIdentifier());
        user.setFullName(entity.getName());
        user.setEmail(entity.getEmail());
        user.setPassword(entity.getPassword());
        if (entity.getRole() != null) {
            user.setRole(UserRole.valueOf(entity.getRole()));
        }
        if (entity.getStatus() != null) {
            user.setStatus(UserStatus.valueOf(entity.getStatus()));
        }
        return user;
    }
}

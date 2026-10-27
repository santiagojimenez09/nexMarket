package application.adapters.persistence.jpa.mappers;

import application.adapters.persistence.jpa.entities.SellerJpaEntity;
import application.domain.models.Administrator;
import application.domain.models.Seller;
import application.domain.valueObjects.UserRole;
import application.domain.valueObjects.UserStatus;

public class SellerJpaMapper {

    public static SellerJpaEntity toEntity(Seller domain) {
        if (domain == null) return null;
        String registeredBy = domain.getRegisteredBy() != null ? domain.getRegisteredBy().getIdentifier() : null;
        return SellerJpaEntity.builder()
                .identifier(domain.getIdentifier())
                .name(domain.getFullName())
                .email(domain.getEmail())
                .registeredByIdentifier(registeredBy)
                .build();
    }

    public static Seller toDomain(SellerJpaEntity entity) {
        if (entity == null) return null;
        Seller seller = new Seller();
        seller.setIdentifier(entity.getIdentifier());
        seller.setFullName(entity.getName());
        seller.setEmail(entity.getEmail());
        seller.setRole(UserRole.SELLER);
        seller.setStatus(UserStatus.ACTIVE);
        if (entity.getRegisteredByIdentifier() != null) {
            Administrator admin = new Administrator();
            admin.setIdentifier(entity.getRegisteredByIdentifier());
            seller.setRegisteredBy(admin);
        }
        return seller;
    }
}

package application.adapters.persistence.jpa.mappers;

import application.adapters.persistence.jpa.entities.BuyerJpaEntity;
import application.domain.models.Buyer;
import application.domain.valueObjects.BuyerCommercialStatus;
import application.domain.valueObjects.UserRole;
import application.domain.valueObjects.UserStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BuyerJpaMapper {

    public static BuyerJpaEntity toEntity(Buyer domain) {
        if (domain == null) return null;
        String additional = null;
        if (domain.getAdditionalAddresses() != null && !domain.getAdditionalAddresses().isEmpty()) {
            additional = String.join(";;", domain.getAdditionalAddresses());
        }
        return BuyerJpaEntity.builder()
                .identifier(domain.getIdentifier())
                .name(domain.getFullName())
                .email(domain.getEmail())
                .primaryAddress(domain.getPrimaryAddress())
                .additionalAddresses(additional)
                .commercialStatus(domain.getCommercialStatus() != null ? domain.getCommercialStatus().name() : null)
                .build();
    }

    public static Buyer toDomain(BuyerJpaEntity entity) {
        if (entity == null) return null;
        Buyer buyer = new Buyer();
        buyer.setIdentifier(entity.getIdentifier());
        buyer.setFullName(entity.getName());
        buyer.setEmail(entity.getEmail());
        buyer.setRole(UserRole.BUYER);
        buyer.setStatus(UserStatus.ACTIVE);
        buyer.setPrimaryAddress(entity.getPrimaryAddress());
        if (entity.getCommercialStatus() != null) {
            try {
                buyer.setCommercialStatus(BuyerCommercialStatus.valueOf(entity.getCommercialStatus()));
            } catch (IllegalArgumentException ignored) {
            }
        }
        if (entity.getAdditionalAddresses() != null && !entity.getAdditionalAddresses().isBlank()) {
            buyer.setAdditionalAddresses(new ArrayList<>(Arrays.asList(entity.getAdditionalAddresses().split(";;"))));
        }
        return buyer;
    }
}

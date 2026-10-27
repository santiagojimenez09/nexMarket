package application.adapters.persistence.jpa.mappers;

import application.adapters.persistence.jpa.entities.InvoiceJpaEntity;
import application.domain.models.Buyer;
import application.domain.models.Invoice;
import application.domain.models.Order;

public class InvoiceJpaMapper {

    public static InvoiceJpaEntity toEntity(Invoice domain) {
        if (domain == null) return null;
        String orderId = domain.getOrder() != null ? domain.getOrder().getIdentifier() : null;
        String buyerId = domain.getBuyer() != null ? domain.getBuyer().getIdentifier() : null;

        return InvoiceJpaEntity.builder()
                .identifier(domain.getIdentifier())
                .orderIdentifier(orderId)
                .buyerIdentifier(buyerId)
                .issueDate(domain.getIssueDate())
                .totalAmount(domain.getTotalAmount())
                .build();
    }

    public static Invoice toDomain(InvoiceJpaEntity entity) {
        if (entity == null) return null;
        Invoice invoice = new Invoice();
        invoice.setIdentifier(entity.getIdentifier());
        invoice.setIssueDate(entity.getIssueDate());
        invoice.setTotalAmount(entity.getTotalAmount());

        if (entity.getOrderIdentifier() != null) {
            Order order = new Order();
            order.setIdentifier(entity.getOrderIdentifier());
            invoice.setOrder(order);
        }
        if (entity.getBuyerIdentifier() != null) {
            Buyer buyer = new Buyer();
            buyer.setIdentifier(entity.getBuyerIdentifier());
            invoice.setBuyer(buyer);
        }
        return invoice;
    }
}

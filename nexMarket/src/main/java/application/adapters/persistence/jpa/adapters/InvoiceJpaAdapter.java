package application.adapters.persistence.jpa.adapters;

import application.adapters.persistence.jpa.entities.InvoiceJpaEntity;
import application.adapters.persistence.jpa.mappers.InvoiceJpaMapper;
import application.adapters.persistence.jpa.repositories.SpringDataJpaInvoiceRepository;
import application.domain.models.Buyer;
import application.domain.models.Invoice;
import application.domain.models.Order;
import application.domain.ports.out.InvoiceRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class InvoiceJpaAdapter implements InvoiceRepositoryPort {

    private final SpringDataJpaInvoiceRepository invoiceRepository;

    @Override
    public Invoice save(Invoice invoice) {
        if (invoice == null) return null;
        InvoiceJpaEntity entity = InvoiceJpaMapper.toEntity(invoice);
        InvoiceJpaEntity saved = invoiceRepository.save(entity);
        return InvoiceJpaMapper.toDomain(saved);
    }

    @Override
    public Optional<Invoice> findByOrder(Order order) {
        if (order == null || order.getIdentifier() == null) return Optional.empty();
        return invoiceRepository.findByOrderIdentifier(order.getIdentifier()).map(InvoiceJpaMapper::toDomain);
    }

    @Override
    public List<Invoice> findByBuyer(Buyer buyer) {
        if (buyer == null || buyer.getIdentifier() == null) {
            return invoiceRepository.findAll().stream().map(InvoiceJpaMapper::toDomain).collect(Collectors.toList());
        }
        return invoiceRepository.findByBuyerIdentifier(buyer.getIdentifier()).stream()
                .map(InvoiceJpaMapper::toDomain)
                .collect(Collectors.toList());
    }
}

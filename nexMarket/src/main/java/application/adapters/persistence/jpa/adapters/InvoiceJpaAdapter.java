package application.adapters.persistence.jpa.adapters;

import application.domain.models.Buyer;
import application.domain.models.Invoice;
import application.domain.models.Order;
import application.domain.ports.out.InvoiceRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class InvoiceJpaAdapter implements InvoiceRepositoryPort {

    private final Map<String, Invoice> store = new ConcurrentHashMap<>();

    @Override
    public Invoice save(Invoice invoice) {
        if (invoice == null || invoice.getIdentifier() == null) return invoice;
        store.put(invoice.getIdentifier(), invoice);
        return invoice;
    }

    @Override
    public Optional<Invoice> findByOrder(Order order) {
        if (order == null || order.getIdentifier() == null) return Optional.empty();
        return store.values().stream()
                .filter(i -> i.getOrder() != null && order.getIdentifier().equals(i.getOrder().getIdentifier()))
                .findFirst();
    }

    @Override
    public List<Invoice> findByBuyer(Buyer buyer) {
        if (buyer == null || buyer.getIdentifier() == null) return List.copyOf(store.values());
        return store.values().stream()
                .filter(i -> i.getBuyer() != null && buyer.getIdentifier().equals(i.getBuyer().getIdentifier()))
                .collect(Collectors.toList());
    }
}

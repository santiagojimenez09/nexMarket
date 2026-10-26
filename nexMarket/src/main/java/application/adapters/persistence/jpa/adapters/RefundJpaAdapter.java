package application.adapters.persistence.jpa.adapters;

import application.domain.models.Refund;
import application.domain.models.Return;
import application.domain.ports.out.RefundRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class RefundJpaAdapter implements RefundRepositoryPort {

    private final Map<String, Refund> store = new ConcurrentHashMap<>();

    @Override
    public Refund save(Refund refund) {
        if (refund == null || refund.getIdentifier() == null) return refund;
        store.put(refund.getIdentifier(), refund);
        return refund;
    }

    @Override
    public Optional<Refund> findByReturn(Return returnRequest) {
        if (returnRequest == null || returnRequest.getIdentifier() == null) return Optional.empty();
        return store.values().stream()
                .filter(r -> r.getRelatedReturn() != null && returnRequest.getIdentifier().equals(r.getRelatedReturn().getIdentifier()))
                .findFirst();
    }

    @Override
    public List<Refund> findByStatus(Refund refund) {
        if (refund == null || refund.getRefundStatus() == null) return List.copyOf(store.values());
        return store.values().stream()
                .filter(r -> refund.getRefundStatus() == r.getRefundStatus())
                .collect(Collectors.toList());
    }

    @Override
    public void update(Refund refund) {
        if (refund != null && refund.getIdentifier() != null) {
            store.put(refund.getIdentifier(), refund);
        }
    }
}

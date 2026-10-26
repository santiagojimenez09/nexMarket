package application.adapters.persistence.jpa.adapters;

import application.domain.models.Order;
import application.domain.models.Return;
import application.domain.ports.out.ReturnRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class ReturnJpaAdapter implements ReturnRepositoryPort {

    private final Map<String, Return> store = new ConcurrentHashMap<>();

    @Override
    public Return save(Return returnRequest) {
        if (returnRequest == null || returnRequest.getIdentifier() == null) return returnRequest;
        store.put(returnRequest.getIdentifier(), returnRequest);
        return returnRequest;
    }

    @Override
    public Optional<Return> findByIdentifier(Return returnRequest) {
        if (returnRequest == null || returnRequest.getIdentifier() == null) return Optional.empty();
        return Optional.ofNullable(store.get(returnRequest.getIdentifier()));
    }

    @Override
    public List<Return> findByOrder(Order order) {
        if (order == null || order.getIdentifier() == null) return List.copyOf(store.values());
        return store.values().stream()
                .filter(r -> r.getOrder() != null && order.getIdentifier().equals(r.getOrder().getIdentifier()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Return> findByStatus(Return returnRequest) {
        if (returnRequest == null || returnRequest.getReturnStatus() == null) return List.copyOf(store.values());
        return store.values().stream()
                .filter(r -> returnRequest.getReturnStatus() == r.getReturnStatus())
                .collect(Collectors.toList());
    }

    @Override
    public void update(Return returnRequest) {
        if (returnRequest != null && returnRequest.getIdentifier() != null) {
            store.put(returnRequest.getIdentifier(), returnRequest);
        }
    }
}

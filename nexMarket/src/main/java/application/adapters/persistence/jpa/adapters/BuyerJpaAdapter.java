package application.adapters.persistence.jpa.adapters;

import application.domain.models.Buyer;
import application.domain.ports.out.BuyerRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class BuyerJpaAdapter implements BuyerRepositoryPort {

    private final Map<String, Buyer> store = new ConcurrentHashMap<>();

    @Override
    public Buyer save(Buyer buyer) {
        if (buyer == null || buyer.getIdentifier() == null) return buyer;
        store.put(buyer.getIdentifier(), buyer);
        return buyer;
    }

    @Override
    public Optional<Buyer> findByIdentifier(Buyer buyer) {
        if (buyer == null || buyer.getIdentifier() == null) return Optional.empty();
        return Optional.ofNullable(store.get(buyer.getIdentifier()));
    }

    @Override
    public void update(Buyer buyer) {
        if (buyer != null && buyer.getIdentifier() != null) {
            store.put(buyer.getIdentifier(), buyer);
        }
    }
}

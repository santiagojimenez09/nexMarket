package application.adapters.persistence.jpa.adapters;

import application.domain.models.Administrator;
import application.domain.models.Seller;
import application.domain.ports.out.SellerRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class SellerJpaAdapter implements SellerRepositoryPort {

    private final Map<String, Seller> store = new ConcurrentHashMap<>();

    @Override
    public Seller save(Seller seller) {
        if (seller == null || seller.getIdentifier() == null) return seller;
        store.put(seller.getIdentifier(), seller);
        return seller;
    }

    @Override
    public Optional<Seller> findByIdentifier(Seller seller) {
        if (seller == null || seller.getIdentifier() == null) return Optional.empty();
        return Optional.ofNullable(store.get(seller.getIdentifier()));
    }

    @Override
    public List<Seller> findByRegisteredBy(Administrator administrator) {
        if (administrator == null || administrator.getIdentifier() == null) {
            return List.copyOf(store.values());
        }
        return store.values().stream()
                .filter(s -> s.getRegisteredBy() != null && administrator.getIdentifier().equals(s.getRegisteredBy().getIdentifier()))
                .collect(Collectors.toList());
    }

    @Override
    public void update(Seller seller) {
        if (seller != null && seller.getIdentifier() != null) {
            store.put(seller.getIdentifier(), seller);
        }
    }
}

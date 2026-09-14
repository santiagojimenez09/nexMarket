package application.domain.ports.out;

import application.domain.models.Administrator;
import application.domain.models.Seller;

import java.util.List;
import java.util.Optional;

public interface SellerRepositoryPort {
    Seller save(Seller seller);
    Optional<Seller> findByIdentifier(Seller seller);
    List<Seller> findByRegisteredBy(Administrator administrator);
    void update(Seller seller);
}

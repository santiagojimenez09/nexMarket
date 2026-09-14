package application.domain.ports.out;

import application.domain.models.Order;
import application.domain.models.Return;

import java.util.List;
import java.util.Optional;

public interface ReturnRepositoryPort {
    Return save(Return returnRequest);
    Optional<Return> findByIdentifier(Return returnRequest);
    List<Return> findByOrder(Order order);
    List<Return> findByStatus(Return returnRequest);
    void update(Return returnRequest);
}

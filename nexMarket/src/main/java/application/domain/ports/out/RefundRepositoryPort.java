package application.domain.ports.out;

import application.domain.models.Refund;
import application.domain.models.Return;

import java.util.List;
import java.util.Optional;

public interface RefundRepositoryPort {
    Refund save(Refund refund);
    Optional<Refund> findByReturn(Return returnRequest);
    List<Refund> findByStatus(Refund refund);
    void update(Refund refund);
}

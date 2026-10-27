package application.adapters.persistence.jpa.adapters;

import application.adapters.persistence.jpa.entities.RefundJpaEntity;
import application.adapters.persistence.jpa.mappers.RefundJpaMapper;
import application.adapters.persistence.jpa.repositories.SpringDataJpaRefundRepository;
import application.domain.models.Refund;
import application.domain.models.Return;
import application.domain.ports.out.RefundRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class RefundJpaAdapter implements RefundRepositoryPort {

    private final SpringDataJpaRefundRepository refundRepository;

    @Override
    public Refund save(Refund refund) {
        if (refund == null) return null;
        RefundJpaEntity entity = RefundJpaMapper.toEntity(refund);
        RefundJpaEntity saved = refundRepository.save(entity);
        return RefundJpaMapper.toDomain(saved);
    }

    @Override
    public Optional<Refund> findByReturn(Return returnRequest) {
        if (returnRequest == null || returnRequest.getIdentifier() == null) return Optional.empty();
        return refundRepository.findByReturnIdentifier(returnRequest.getIdentifier()).map(RefundJpaMapper::toDomain);
    }

    @Override
    public List<Refund> findByStatus(Refund refund) {
        if (refund == null || refund.getRefundStatus() == null) {
            return refundRepository.findAll().stream().map(RefundJpaMapper::toDomain).collect(Collectors.toList());
        }
        return refundRepository.findByRefundStatus(refund.getRefundStatus().name()).stream()
                .map(RefundJpaMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void update(Refund refund) {
        if (refund != null && refund.getIdentifier() != null) {
            refundRepository.save(RefundJpaMapper.toEntity(refund));
        }
    }
}

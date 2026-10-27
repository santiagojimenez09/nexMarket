package application.adapters.persistence.jpa.adapters;

import application.adapters.persistence.jpa.entities.ReturnJpaEntity;
import application.adapters.persistence.jpa.mappers.ReturnJpaMapper;
import application.adapters.persistence.jpa.repositories.SpringDataJpaReturnRepository;
import application.domain.models.Order;
import application.domain.models.Return;
import application.domain.ports.out.ReturnRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ReturnJpaAdapter implements ReturnRepositoryPort {

    private final SpringDataJpaReturnRepository returnRepository;

    @Override
    public Return save(Return returnRequest) {
        if (returnRequest == null) return null;
        ReturnJpaEntity entity = ReturnJpaMapper.toEntity(returnRequest);
        ReturnJpaEntity saved = returnRepository.save(entity);
        return ReturnJpaMapper.toDomain(saved);
    }

    @Override
    public Optional<Return> findByIdentifier(Return returnRequest) {
        if (returnRequest == null || returnRequest.getIdentifier() == null) return Optional.empty();
        return returnRepository.findById(returnRequest.getIdentifier()).map(ReturnJpaMapper::toDomain);
    }

    @Override
    public List<Return> findByOrder(Order order) {
        if (order == null || order.getIdentifier() == null) {
            return returnRepository.findAll().stream().map(ReturnJpaMapper::toDomain).collect(Collectors.toList());
        }
        return returnRepository.findByOrderIdentifier(order.getIdentifier()).stream()
                .map(ReturnJpaMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Return> findByStatus(Return returnRequest) {
        if (returnRequest == null || returnRequest.getReturnStatus() == null) {
            return returnRepository.findAll().stream().map(ReturnJpaMapper::toDomain).collect(Collectors.toList());
        }
        return returnRepository.findByReturnStatus(returnRequest.getReturnStatus().name()).stream()
                .map(ReturnJpaMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void update(Return returnRequest) {
        if (returnRequest != null && returnRequest.getIdentifier() != null) {
            returnRepository.save(ReturnJpaMapper.toEntity(returnRequest));
        }
    }
}

package application.adapters.persistence.jpa.adapters;

import application.adapters.persistence.jpa.entities.OperationJpaEntity;
import application.adapters.persistence.jpa.mappers.OperationJpaMapper;
import application.adapters.persistence.jpa.repositories.SpringDataJpaOperationRepository;
import application.domain.models.Operation;
import application.domain.models.TrackableProcess;
import application.domain.models.User;
import application.domain.ports.out.OperationRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OperationJpaAdapter implements OperationRepositoryPort {

    private final SpringDataJpaOperationRepository operationRepository;

    @Override
    public Operation save(Operation operation) {
        if (operation == null) return null;
        OperationJpaEntity entity = OperationJpaMapper.toEntity(operation);
        OperationJpaEntity saved = operationRepository.save(entity);
        return OperationJpaMapper.toDomain(saved);
    }

    @Override
    public Optional<Operation> findById(Operation operation) {
        if (operation == null || operation.getOperationId() == null) return Optional.empty();
        return operationRepository.findById(operation.getOperationId()).map(OperationJpaMapper::toDomain);
    }

    @Override
    public List<Operation> findByUser(User user) {
        if (user == null || user.getIdentifier() == null) {
            return operationRepository.findAll().stream().map(OperationJpaMapper::toDomain).collect(Collectors.toList());
        }
        return operationRepository.findByPerformedByIdentifier(user.getIdentifier()).stream()
                .map(OperationJpaMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Operation> findByProcess(TrackableProcess process) {
        if (process == null || process.getIdentifier() == null) {
            return operationRepository.findAll().stream().map(OperationJpaMapper::toDomain).collect(Collectors.toList());
        }
        return operationRepository.findByAffectedProcessIdentifier(process.getIdentifier()).stream()
                .map(OperationJpaMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Operation> findByType(Operation operation) {
        if (operation == null || operation.getOperationType() == null) {
            return operationRepository.findAll().stream().map(OperationJpaMapper::toDomain).collect(Collectors.toList());
        }
        return operationRepository.findByOperationType(operation.getOperationType().name()).stream()
                .map(OperationJpaMapper::toDomain)
                .collect(Collectors.toList());
    }
}

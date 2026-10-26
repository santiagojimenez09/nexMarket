package application.adapters.persistence.jpa.adapters;

import application.domain.models.Operation;
import application.domain.models.TrackableProcess;
import application.domain.models.User;
import application.domain.ports.out.OperationRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class OperationJpaAdapter implements OperationRepositoryPort {

    private final Map<String, Operation> store = new ConcurrentHashMap<>();

    @Override
    public Operation save(Operation operation) {
        if (operation == null || operation.getOperationId() == null) return operation;
        store.put(operation.getOperationId(), operation);
        return operation;
    }

    @Override
    public Optional<Operation> findById(Operation operation) {
        if (operation == null || operation.getOperationId() == null) return Optional.empty();
        return Optional.ofNullable(store.get(operation.getOperationId()));
    }

    @Override
    public List<Operation> findByUser(User user) {
        if (user == null || user.getIdentifier() == null) return List.copyOf(store.values());
        return store.values().stream()
                .filter(op -> op.getPerformedBy() != null && user.getIdentifier().equals(op.getPerformedBy().getIdentifier()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Operation> findByProcess(TrackableProcess process) {
        if (process == null || process.getIdentifier() == null) return List.copyOf(store.values());
        return store.values().stream()
                .filter(op -> op.getAffectedProcess() != null && process.getIdentifier().equals(op.getAffectedProcess().getIdentifier()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Operation> findByType(Operation operation) {
        if (operation == null || operation.getOperationType() == null) return List.copyOf(store.values());
        return store.values().stream()
                .filter(op -> operation.getOperationType() == op.getOperationType())
                .collect(Collectors.toList());
    }
}

package application.domain.ports.out;

import application.domain.models.Operation;
import application.domain.models.TrackableProcess;
import application.domain.models.User;

import java.util.List;
import java.util.Optional;

public interface OperationRepositoryPort {
    Operation save(Operation operation);
    Optional<Operation> findById(Operation operation);
    List<Operation> findByUser(User user);
    List<Operation> findByProcess(TrackableProcess process);
    List<Operation> findByType(Operation operation);
}

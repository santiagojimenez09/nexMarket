package application.domain.ports.out;

import application.domain.models.User;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryPort {
    User save(User user);
    Optional<User> findByIdentifier(User user);
    Optional<User> findByEmail(User user);
    boolean existsByIdentifier(User user);
    boolean existsByEmail(User user);
    List<User> findByRole(User user);
    void update(User user);
}

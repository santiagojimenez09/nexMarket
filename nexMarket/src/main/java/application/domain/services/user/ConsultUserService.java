package application.domain.services.user;

import application.domain.models.User;
import application.domain.exceptions.EntityNotFoundException;
import application.domain.ports.out.UserRepositoryPort;

import java.util.List;

public class ConsultUserService {

    private final UserRepositoryPort userRepositoryPort;

    public ConsultUserService(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    public User getByIdentifier(User user) {
        return userRepositoryPort.findByIdentifier(user)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con identificador: " + user.getIdentifier()));
    }

    public User getByEmail(User user) {
        return userRepositoryPort.findByEmail(user)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con email: " + user.getEmail()));
    }

    public List<User> getByRole(User user) {
        return userRepositoryPort.findByRole(user);
    }
}

package application.domain.services.user;

import application.domain.models.User;
import application.domain.exceptions.DomainException;
import application.domain.ports.out.PasswordServicePort;
import application.domain.ports.out.UserRepositoryPort;
import application.domain.valueObjects.UserStatus;

public class RegisterUserService {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordServicePort passwordServicePort;

    public RegisterUserService(UserRepositoryPort userRepositoryPort, PasswordServicePort passwordServicePort) {
        this.userRepositoryPort = userRepositoryPort;
        this.passwordServicePort = passwordServicePort;
    }

    public User registerUser(User user, String rawPassword) {
        if (user == null) {
            throw new DomainException("El usuario no puede ser nulo.");
        }
        if (user.getIdentifier() == null || user.getIdentifier().trim().isEmpty()) {
            throw new DomainException("El identificador del usuario es obligatorio.");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new DomainException("El correo electrónico del usuario es obligatorio.");
        }
        if (user.getRole() == null) {
            throw new DomainException("El rol del usuario es obligatorio (RG-02).");
        }

        if (userRepositoryPort.existsByIdentifier(user)) {
            throw new DomainException("Ya existe un usuario con el identificador proporcionado: " + user.getIdentifier());
        }
        if (userRepositoryPort.existsByEmail(user)) {
            throw new DomainException("Ya existe un usuario con el correo electrónico proporcionado: " + user.getEmail());
        }

        if (rawPassword != null && !rawPassword.trim().isEmpty()) {
            user.setPassword(passwordServicePort.encrypt(rawPassword));
        }

        if (user.getStatus() == null) {
            user.setStatus(UserStatus.ACTIVE);
        }

        return userRepositoryPort.save(user);
    }
}

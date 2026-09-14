package application.domain.services.user;

import application.domain.models.User;
import application.domain.exceptions.EntityNotFoundException;
import application.domain.exceptions.UnauthorizedOperationException;
import application.domain.ports.out.JwtServicePort;
import application.domain.ports.out.PasswordServicePort;
import application.domain.ports.out.UserRepositoryPort;
import application.domain.valueObjects.UserStatus;

public class AuthenticateUserService {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordServicePort passwordServicePort;
    private final JwtServicePort jwtServicePort;

    public AuthenticateUserService(UserRepositoryPort userRepositoryPort,
                                   PasswordServicePort passwordServicePort,
                                   JwtServicePort jwtServicePort) {
        this.userRepositoryPort = userRepositoryPort;
        this.passwordServicePort = passwordServicePort;
        this.jwtServicePort = jwtServicePort;
    }

    public String authenticate(User user, String rawPassword) {
        User existingUser = userRepositoryPort.findByEmail(user)
                .orElseThrow(() -> new EntityNotFoundException("Credenciales inválidas."));

        if (existingUser.getStatus() == UserStatus.BLOCKED) {
            throw new UnauthorizedOperationException("El acceso del usuario se encuentra bloqueado.");
        }
        if (existingUser.getStatus() == UserStatus.INACTIVE) {
            throw new UnauthorizedOperationException("El usuario está inactivo.");
        }

        if (!passwordServicePort.matches(rawPassword, existingUser.getPassword())) {
            throw new UnauthorizedOperationException("Credenciales inválidas.");
        }

        return jwtServicePort.generateToken(existingUser);
    }
}

package application.domain.services.user;

import application.domain.models.User;
import application.domain.exceptions.EntityNotFoundException;
import application.domain.exceptions.InvalidStatusTransitionException;
import application.domain.ports.out.UserRepositoryPort;
import application.domain.valueObjects.UserStatus;

public class ChangeUserStatusService {

    private final UserRepositoryPort userRepositoryPort;

    public ChangeUserStatusService(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    public void changeStatus(User user, UserStatus newStatus) {
        if (newStatus == null) {
            throw new InvalidStatusTransitionException("El nuevo estado no puede ser nulo.");
        }

        User existingUser = userRepositoryPort.findByIdentifier(user)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado: " + user.getIdentifier()));

        existingUser.setStatus(newStatus);
        userRepositoryPort.update(existingUser);
    }
}

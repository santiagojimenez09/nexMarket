package application.domain.services.user;

import application.domain.models.User;
import application.domain.exceptions.DomainException;
import application.domain.exceptions.EntityNotFoundException;
import application.domain.ports.out.UserRepositoryPort;

public class UpdateUserService {

    private final UserRepositoryPort userRepositoryPort;

    public UpdateUserService(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    public void updateUser(User updatedUserData) {
        if (updatedUserData == null || updatedUserData.getIdentifier() == null) {
            throw new DomainException("Los datos de usuario para actualización son inválidos.");
        }

        User existingUser = userRepositoryPort.findByIdentifier(updatedUserData)
                .orElseThrow(() -> new EntityNotFoundException("No se puede actualizar; usuario no encontrado: " + updatedUserData.getIdentifier()));

        if (updatedUserData.getFullName() != null && !updatedUserData.getFullName().trim().isEmpty()) {
            existingUser.setFullName(updatedUserData.getFullName());
        }

        userRepositoryPort.update(existingUser);
    }
}

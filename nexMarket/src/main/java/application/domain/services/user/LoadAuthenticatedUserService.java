package application.domain.services.user;

import application.domain.exceptions.DomainException;
import application.domain.exceptions.EntityNotFoundException;
import application.domain.models.Buyer;
import application.domain.models.User;
import application.domain.ports.out.UserRepositoryPort;

/**
 * Servicio de dominio para cargar el usuario autenticado por su identificador inmutable.
 * Dominio puro: no conoce Spring Security ni UserDetails.
 */
public class LoadAuthenticatedUserService {

    private final UserRepositoryPort userRepositoryPort;

    public LoadAuthenticatedUserService(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    public User loadByIdentifier(String identifier) {
        if (identifier == null || identifier.isBlank()) {
            throw new DomainException("El identificador del usuario autenticado es requerido.");
        }
        User probe = new Buyer();
        probe.setIdentifier(identifier);
        return userRepositoryPort.findByIdentifier(probe)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con identificador: " + identifier));
    }
}

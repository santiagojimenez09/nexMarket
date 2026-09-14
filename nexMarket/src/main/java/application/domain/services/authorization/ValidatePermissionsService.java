package application.domain.services.authorization;

import application.domain.models.Buyer;
import application.domain.models.TrackableProcess;
import application.domain.models.User;
import application.domain.exceptions.UnauthorizedOperationException;
import application.domain.ports.out.AuthorizationPort;
import application.domain.valueObjects.UserRole;
import application.domain.valueObjects.UserStatus;

public class ValidatePermissionsService {

    private final AuthorizationPort authorizationPort;

    public ValidatePermissionsService(AuthorizationPort authorizationPort) {
        this.authorizationPort = authorizationPort;
    }

    public void validateRole(User user, UserRole requiredRole) {
        if (user == null || user.getRole() == null) {
            throw new UnauthorizedOperationException("Usuario no autenticado o sin rol asignado (RG-02).");
        }
        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new UnauthorizedOperationException("El usuario no se encuentra activo.");
        }
        if (user.getRole() != requiredRole) {
            throw new UnauthorizedOperationException(
                    String.format("Acceso denegado. Se requiere el rol %s, pero el usuario posee %s (RG-03).",
                            requiredRole, user.getRole()));
        }
    }

    public void validateBuyerAccess(User user, Buyer targetBuyer) {
        if (!authorizationPort.isAuthorized(user, targetBuyer)) {
            throw new UnauthorizedOperationException("El usuario no tiene autorización para acceder a los datos de este comprador.");
        }
    }

    public void validateProcessAccess(User user, TrackableProcess process) {
        if (!authorizationPort.canOperateOn(user, process)) {
            throw new UnauthorizedOperationException("El usuario no tiene autorización para operar sobre este proceso.");
        }
    }
}

package application.infrastructure.security;

import application.domain.models.Buyer;
import application.domain.models.TrackableProcess;
import application.domain.models.User;
import application.domain.ports.out.AuthorizationPort;
import application.domain.valueObjects.UserRole;
import org.springframework.stereotype.Component;

@Component
public class DefaultAuthorizationAdapter implements AuthorizationPort {

    @Override
    public boolean isAuthorized(User user, Buyer buyer) {
        if (user == null || buyer == null) return false;
        if (user.getRole() == UserRole.ADMINISTRATOR || user.getRole() == UserRole.SUPERVISOR) {
            return true;
        }
        return user.getIdentifier() != null && user.getIdentifier().equals(buyer.getIdentifier());
    }

    @Override
    public boolean canOperateOn(User user, TrackableProcess process) {
        if (user == null || user.getRole() == null) return false;
        return switch (user.getRole()) {
            case ADMINISTRATOR, SUPERVISOR, LOGISTICS_OPERATOR -> true;
            default -> false;
        };
    }

    @Override
    public boolean canApprove(User user, TrackableProcess process) {
        if (user == null || user.getRole() == null) return false;
        return user.getRole() == UserRole.ADMINISTRATOR || user.getRole() == UserRole.SUPERVISOR;
    }
}

package application.domain.ports.out;

import application.domain.models.Buyer;
import application.domain.models.TrackableProcess;
import application.domain.models.User;

public interface AuthorizationPort {
    boolean isAuthorized(User user, Buyer buyer);
    boolean canOperateOn(User user, TrackableProcess process);
    boolean canApprove(User user, TrackableProcess process);
}

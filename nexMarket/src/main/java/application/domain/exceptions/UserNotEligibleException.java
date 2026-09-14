package application.domain.exceptions;

public class UserNotEligibleException extends DomainException {
    public UserNotEligibleException(String message) {
        super(message);
    }
}

package application.domain.exceptions;

public class InvalidOrderStatusException extends DomainException {
    public InvalidOrderStatusException(String message) {
        super(message);
    }
}

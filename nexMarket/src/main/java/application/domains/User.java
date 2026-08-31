package application.domains;
import application.valueObjects.UserRole;
import application.valueObjects.UserStatus;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class User {
    private String identifier;
    private String fullName;
    private String email;
    private UserRole role;
    private UserStatus status;
}

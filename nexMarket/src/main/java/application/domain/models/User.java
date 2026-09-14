package application.domain.models;
import application.domain.valueObjects.UserRole;
import application.domain.valueObjects.UserStatus;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class User {
    private String identifier;
    private String fullName;
    private String email;
    private String password;
    private UserRole role;
    private UserStatus status;
}

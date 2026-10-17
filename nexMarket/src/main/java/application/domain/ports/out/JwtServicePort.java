package application.domain.ports.out;

import application.domain.models.User;
import io.jsonwebtoken.Claims;

public interface JwtServicePort {
    String generateToken(User user);
    boolean validateToken(String token);
    Claims getClaims(String token);
    String extractUserIdentifier(String token);
    Integer extractTokenVersion(String token);
    User reconstructUser(String token);
    String extractUsername(String token);
}

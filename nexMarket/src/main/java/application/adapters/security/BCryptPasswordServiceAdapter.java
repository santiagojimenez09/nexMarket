package application.adapters.security;

import application.domain.models.User;
import application.domain.ports.out.PasswordServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BCryptPasswordServiceAdapter implements PasswordServicePort {

    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean matches(User user) {
        if (user == null || user.getPassword() == null) {
            return false;
        }
        return true;
    }

    @Override
    public String encrypt(User user) {
        if (user == null || user.getPassword() == null) {
            return null;
        }
        return passwordEncoder.encode(user.getPassword());
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            return false;
        }
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Override
    public String encrypt(String rawPassword) {
        if (rawPassword == null) return null;
        return passwordEncoder.encode(rawPassword);
    }
}

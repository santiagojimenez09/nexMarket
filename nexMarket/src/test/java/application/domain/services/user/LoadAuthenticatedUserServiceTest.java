package application.domain.services.user;

import application.domain.exceptions.DomainException;
import application.domain.exceptions.EntityNotFoundException;
import application.domain.models.Buyer;
import application.domain.models.User;
import application.domain.ports.out.UserRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LoadAuthenticatedUserServiceTest {

    private FakeUsers users;
    private LoadAuthenticatedUserService service;

    @BeforeEach
    void setUp() {
        users = new FakeUsers();
        service = new LoadAuthenticatedUserService(users);
    }

    @Test
    void loadsAuthoritativeUserByIdentifier() {
        User stored = new Buyer();
        stored.setIdentifier("USR-01");
        stored.setEmail("test@nexmarket.com");
        users.save(stored);

        User result = service.loadByIdentifier("USR-01");
        assertEquals("USR-01", result.getIdentifier());
        assertEquals("test@nexmarket.com", result.getEmail());
    }

    @Test
    void rejectsUnknownUserWithDomainException() {
        assertThrows(EntityNotFoundException.class, () -> service.loadByIdentifier("UNKNOWN"));
    }

    @Test
    void rejectsNullOrBlankIdWithDomainException() {
        assertThrows(DomainException.class, () -> service.loadByIdentifier(null));
        assertThrows(DomainException.class, () -> service.loadByIdentifier("   "));
    }

    static class FakeUsers implements UserRepositoryPort {
        private final Map<String, User> byId = new HashMap<>();

        @Override
        public User save(User user) {
            byId.put(user.getIdentifier(), user);
            return user;
        }

        @Override
        public Optional<User> findByIdentifier(User user) {
            return Optional.ofNullable(byId.get(user.getIdentifier()));
        }

        @Override
        public Optional<User> findByEmail(User user) {
            return byId.values().stream().filter(u -> u.getEmail().equals(user.getEmail())).findFirst();
        }

        @Override
        public boolean existsByIdentifier(User user) {
            return byId.containsKey(user.getIdentifier());
        }

        @Override
        public boolean existsByEmail(User user) {
            return byId.values().stream().anyMatch(u -> u.getEmail().equals(user.getEmail()));
        }

        @Override
        public List<User> findByRole(User user) {
            return byId.values().stream().filter(u -> u.getRole() == user.getRole()).toList();
        }

        @Override
        public void update(User user) {
            byId.put(user.getIdentifier(), user);
        }
    }
}

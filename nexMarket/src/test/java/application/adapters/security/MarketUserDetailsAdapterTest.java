package application.adapters.security;

import application.adapters.security.dtos.AuthenticatedUserPrincipal;
import application.domain.models.Buyer;
import application.domain.models.User;
import application.domain.ports.out.UserRepositoryPort;
import application.domain.services.user.LoadAuthenticatedUserService;
import application.domain.valueObjects.UserRole;
import application.domain.valueObjects.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MarketUserDetailsAdapterTest {

    private FakeUsers users;
    private MarketUserDetailsAdapter adapter;

    @BeforeEach
    void setUp() {
        users = new FakeUsers();
        adapter = new MarketUserDetailsAdapter(new LoadAuthenticatedUserService(users));
    }

    @Test
    void loadsPrincipalFromAuthoritativeModel() {
        User stored = new Buyer();
        stored.setIdentifier("USR-11");
        stored.setEmail("buyer1@nexmarket.com");
        stored.setFullName("Santiago Comprador");
        stored.setRole(UserRole.BUYER);
        stored.setStatus(UserStatus.ACTIVE);
        stored.setAuthTokenVersion(2);
        users.save(stored);

        AuthenticatedUserPrincipal principal =
                (AuthenticatedUserPrincipal) adapter.loadUserByUsername("USR-11");

        assertEquals("USR-11", principal.getUser().getIdentifier());
        assertEquals("buyer1@nexmarket.com", principal.getUsername());
        assertTrue(principal.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_BUYER")));
        assertTrue(principal.isEnabled());
    }

    @Test
    void rejectsUnknownUsers() {
        assertThrows(UsernameNotFoundException.class,
                () -> adapter.loadUserByUsername("UNKNOWN-USER"));
    }

    @Test
    void rejectsInvalidSubjects() {
        assertThrows(UsernameNotFoundException.class,
                () -> adapter.loadUserByUsername(null));
        assertThrows(UsernameNotFoundException.class,
                () -> adapter.loadUserByUsername("   "));
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

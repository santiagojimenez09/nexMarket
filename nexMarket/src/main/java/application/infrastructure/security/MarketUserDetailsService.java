package application.infrastructure.security;

import application.domain.models.Buyer;
import application.domain.models.User;
import application.domain.ports.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MarketUserDetailsService implements UserDetailsService {

    private final UserRepositoryPort userRepositoryPort;

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        Buyer query = new Buyer();
        query.setIdentifier(identifier);
        query.setEmail(identifier);

        User user = userRepositoryPort.findByIdentifier(query)
                .or(() -> userRepositoryPort.findByEmail(query))
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con identificador o correo: " + identifier));

        return new AuthenticatedUserPrincipal(user);
    }
}

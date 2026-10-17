package application.adapters.security;

import application.adapters.security.dtos.AuthenticatedUserPrincipal;
import application.domain.exceptions.DomainException;
import application.domain.models.User;
import application.domain.services.user.LoadAuthenticatedUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Adaptador de seguridad (Spring UserDetailsService) hacia el dominio.
 * Resuelve el User autoritativo vía LoadAuthenticatedUserService.
 */
@Service
@RequiredArgsConstructor
public class MarketUserDetailsAdapter implements UserDetailsService {

    private final LoadAuthenticatedUserService loadAuthenticatedUserService;

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        if (identifier == null || identifier.isBlank()) {
            throw new UsernameNotFoundException("Identificador de usuario inválido.");
        }
        try {
            User stored = loadAuthenticatedUserService.loadByIdentifier(identifier);
            return new AuthenticatedUserPrincipal(stored);
        } catch (DomainException e) {
            throw new UsernameNotFoundException("Usuario no encontrado.", e);
        }
    }
}

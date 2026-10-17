package application.adapters.security;

import application.adapters.security.dtos.AuthenticatedUserPrincipal;
import application.domain.models.User;
import application.domain.valueObjects.UserStatus;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = extractBearerToken(request);

        if (token != null && jwtProvider.validateToken(token)) {
            Integer tokenVersion = jwtProvider.extractTokenVersion(token);

            try {
                // Reconstrucción del User completo desde los claims del token sin round-trip a BD
                User current = jwtProvider.reconstructUser(token);
                AuthenticatedUserPrincipal principal = new AuthenticatedUserPrincipal(current);

                if (tokenVersion != null && isUsable(current, tokenVersion)) {
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(
                                    principal, null, principal.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                } else {
                    SecurityContextHolder.clearContext();
                }
            } catch (Exception e) {
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean isUsable(User stored, Integer tokenVersion) {
        if (stored == null || !UserStatus.ACTIVE.equals(stored.getStatus())) {
            return false;
        }
        Integer storedVersion = stored.getAuthTokenVersion();
        if (storedVersion == null) {
            storedVersion = 1;
        }
        return storedVersion.equals(tokenVersion);
    }

    private String extractBearerToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}

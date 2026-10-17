package application.adapters.security;

import application.adapters.security.mappers.JwtClaimsMapper;
import application.domain.models.User;
import application.domain.ports.out.JwtServicePort;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

/**
 * Adaptador de salida para emisión y validación de JWT.
 * Embebe el snapshot de User como claims para autenticar/autorizar sin round trip a BD.
 */
@Component
@RequiredArgsConstructor
public class JwtProvider implements JwtServicePort {

    @Value("${jwt.secret:TDEATDEATDEATDEATDEATDEATDEATDEA}")
    private String secret;

    @Value("${jwt.expiration-ms:3600000}")
    private long expirationMs;

    private SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String generateToken(User user) {
        if (user == null || user.getIdentifier() == null) {
            throw new IllegalArgumentException("User with an identifier must be provided to issue a token.");
        }
        Instant now = Instant.now();
        Instant expiry = now.plusMillis(expirationMs);

        JwtBuilder builder = Jwts.builder()
                .subject(user.getIdentifier())
                .id(UUID.randomUUID().toString())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry));
        JwtClaimsMapper.applyClaims(builder, user);
        return builder.signWith(getSigningKey()).compact();
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    @Override
    public String extractUserIdentifier(String token) {
        try {
            return JwtClaimsMapper.extractUserIdentifier(getClaims(token));
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Integer extractTokenVersion(String token) {
        try {
            return JwtClaimsMapper.extractTokenVersion(getClaims(token));
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public User reconstructUser(String token) {
        return JwtClaimsMapper.toDomain(getClaims(token));
    }

    @Override
    public String extractUsername(String token) {
        try {
            Claims claims = getClaims(token);
            String email = claims.get("email", String.class);
            return email != null ? email : claims.getSubject();
        } catch (Exception e) {
            return null;
        }
    }
}

package application.infrastructure.security;

import application.domain.models.User;
import application.domain.ports.out.JwtServicePort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

@Component
public class JwtProvider implements JwtServicePort {

    private final String secretKey;

    public JwtProvider(@Value("${jwt.secret:default-marketplace-secret-key-32-chars-long!}") String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public String generateToken(User user) {
        if (user == null || user.getIdentifier() == null) {
            return null;
        }
        long now = System.currentTimeMillis();
        long exp = now + 3600000L; // 1 hour
        String payload = user.getIdentifier() + ":" + (user.getRole() != null ? user.getRole().name() : "") + ":" + exp;
        String encodedPayload = Base64.getUrlEncoder().withoutPadding().encodeToString(payload.getBytes(StandardCharsets.UTF_8));
        String signature = sign(encodedPayload);
        return encodedPayload + "." + signature;
    }

    @Override
    public boolean validateToken(String token) {
        if (token == null || !token.contains(".")) {
            return false;
        }
        String[] parts = token.split("\\.");
        if (parts.length != 2) {
            return false;
        }
        String expectedSignature = sign(parts[0]);
        if (!MessageDigest.isEqual(parts[1].getBytes(StandardCharsets.UTF_8), expectedSignature.getBytes(StandardCharsets.UTF_8))) {
            return false;
        }
        try {
            String decoded = new String(Base64.getUrlDecoder().decode(parts[0]), StandardCharsets.UTF_8);
            String[] data = decoded.split(":");
            if (data.length < 3) return false;
            long exp = Long.parseLong(data[2]);
            return System.currentTimeMillis() <= exp;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String extractUsername(String token) {
        if (!validateToken(token)) return null;
        try {
            String[] parts = token.split("\\.");
            String decoded = new String(Base64.getUrlDecoder().decode(parts[0]), StandardCharsets.UTF_8);
            return decoded.split(":")[0];
        } catch (Exception e) {
            return null;
        }
    }

    private String sign(String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] rawHmac = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(rawHmac);
        } catch (Exception e) {
            throw new RuntimeException("Error calculating HMAC signature", e);
        }
    }
}

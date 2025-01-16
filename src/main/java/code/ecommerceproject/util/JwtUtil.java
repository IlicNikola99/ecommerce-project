package code.ecommerceproject.util;

import code.ecommerceproject.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    private final JwtConfig jwtConfig;
    private final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    @Autowired
    public JwtUtil(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    public String generateToken(String subject, Map<String, Object> claims) {
        final long expirationMillis = jwtConfig.getExpirationHours() * 60 * 60 * 1000L;

        return Jwts.builder()
                .setSubject(subject)
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractEmail(String token) {
        final Claims claims = extractClaims(token);
        return (String) claims.get("name");
    }

    public String extractRoles(String token) {
        final Claims claims = extractClaims(token);
        return (String) claims.get("role");
    }

    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    public boolean validateToken(String token, String username) {
        return (username.equals(extractEmail(token)) && !isTokenExpired(token));
    }
}
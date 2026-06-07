package com.rental.system.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@SuppressWarnings("null")
public class JwtTokenProvider {

    private static final String JWT_SECRET = "M2I4Yjg5YTYzZTM0OTY1Y2QxZDY4OGU2NGRmOTBiZGYzOGFjNjc0ZDQ5MTYyNmNjMWRjODlkZTVhMzliOWI0Mg==";
    private static final long JWT_EXPIRATION = 86400000; // 24 hours

    private final SecretKey key;

    public JwtTokenProvider() {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(JWT_SECRET));
    }

    public String generateToken(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        String username;
        String role;

        if (principal instanceof StaffPrincipal staffPrincipal) {
            username = staffPrincipal.getUsername();
            role = "STAFF";
        } else if (principal instanceof CustomerPrincipal customerPrincipal) {
            username = customerPrincipal.getUsername();
            role = "CUSTOMER";
        } else {
            throw new IllegalArgumentException("Unknown principal type");
        }

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);

        return Jwts.builder()
                .subject(username)
                .claim("user_type", role)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key)
                .compact();
    }

    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    public String getRoleFromJWT(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.get("user_type", String.class);
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(authToken);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            // Token validation failed
        }
        return false;
    }
}

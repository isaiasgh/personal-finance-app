package com.isaias.finance.user_service.config.security;

import com.isaias.finance.user_service.data.dto.UserBasicDTO;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtProvider {
    private long EXPIRATION_TIME;

    private final SecretKey key;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    public JwtProvider(@Value("${SECURITY_CONSTANT}") String securityConstant, @Value("${EXPIRATION_TIME}") Long expirationTime) {
        this.key = Keys.hmacShaKeyFor(securityConstant.getBytes(StandardCharsets.UTF_8));
        this.EXPIRATION_TIME = expirationTime;
    }

    public String generateToken (Authentication authentication) {
        String username = authentication.getName();
        UserBasicDTO user = customUserDetailsService.loadUserBasicDTOByUsername(username);
        Date currentTime = new Date ();
        Date expirationTime = new Date (currentTime.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(currentTime)
                .setExpiration(expirationTime)
                .claim("id", user.getId())
                .claim("name", user.getName())
                .claim("lastName", user.getLastName())
                .claim("email", user.getEmail())
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String getUsernameFromJWT (String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean validateToken (String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            return true;
        } catch (ExpiredJwtException ex) {
            throw ex;
        } catch (JwtException ex) {
            throw new AuthenticationCredentialsNotFoundException("Invalid JWT token: " + ex.getMessage());
        }
    }
}
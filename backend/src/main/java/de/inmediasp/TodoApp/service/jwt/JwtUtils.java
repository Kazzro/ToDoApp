package de.inmediasp.TodoApp.service.jwt;


import de.inmediasp.TodoApp.service.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    @Value("${de.inmediasp.TodoApp.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    private SecretKey key;

    @PostConstruct
    protected void init() {
        key = Keys.secretKeyFor(signatureAlgorithm);
    }

    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    public String getUsernameFromHeader(MultiValueMap<String, String> headers) {
        String authorizationPart = String.valueOf(headers.get(AUTHORIZATION));
        if (StringUtils.hasText(authorizationPart) && authorizationPart.startsWith(BEARER))
            return getUserNameFromJwtToken(authorizationPart.substring(7));
        else
            return null;
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getSubject();
    }


    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parse(authToken);

            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

}

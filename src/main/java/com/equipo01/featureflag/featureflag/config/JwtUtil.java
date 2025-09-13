package com.equipo01.featureflag.featureflag.config;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Utility for working with JWT tokens.
 * -Generates JWT tokens signed with a secret key.
 * -Validates JWT tokens and extracts information (such as the user name).
 * 
 * @author alex
 */
@Component
public class JwtUtil {

    // Secret key for signing and validating JWT tokens
    @Value("${JWT_SECRET_KEY}")
    private String secret;
    @Value("${ACCESS_TOKEN_EXPIRATION}")
    private Long expiration;

    /**
     * Generates a JWT token for an authenticated user.
     * -1. Extracts the username from the Authentication object.
     * -2. Creates a token with the username as subject, the issued and expiration dates.
     * -3. Signs the token with the secret key using HS256.
     * -4. Returns the generated token.
     * @param auth
     * @return
     */
    public String generateToken(Authentication auth) {
        String username = auth.getName();
        Date now = new Date();
        Date exp = new Date(now.getTime() + expiration);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    /**
     * Method that extracts the username (subject) from a JWT token.
     * -1. Parse the token using the secret key.
     * -2. Extract the subject (username) from the claims.
     * -3. Return the username.
     * @param token
     * @return
     */
    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    /**
     * Validates that a JWT token is correct.
     * -1. Attempts to parse the token using the secret key.
     * -2. If the token is valid, returns true.
     * -3. If the token is invalid or has expired, catches the exception and returns false.
     *
     * @param token signed JWT token.
     * @return true if the token is valid, false if it is invalid or has expired.
     */

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

}

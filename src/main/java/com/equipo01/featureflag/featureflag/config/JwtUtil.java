package com.equipo01.featureflag.featureflag.config;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;


@Component
public class JwtUtil {

    // Clave secreta para firmar y validar tokens obtenida a partir del archivo
    // devcontainer.env
    @Value("${JWT_SECRET_KEY}")
    private String secret;

    @Value("${ACCESS_TOKEN_EXPIRATION}")
    private Long expiration;

    /**
     * Función que se encarga de generar un token en la fecha actual del usuario
     * especificado.
     *
     * @param auth para obtener el posterior nombre de la persona que se esta
     *             autenticando
     * @return Cadena JWT firmada con la clave secreta y con fecha de caducidad.
     *
     *         Proceso: 1.Obtiene la fecha y hora actuales 2.Calcula la fecha de
     *         expiración sumando el tiempo definido 3.Construye el token con:
     *         -Subject:
     *         nombre de usuario -Fecha de emisión. -Fecha de expiración. -Firma con
     *         el
     *         algoritmo HS512 y la clave secreta. 4.Devuelve el token en formato
     *         String.
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
     * Obtiene el nombre de usuario (subject) desde un token JWT.
     *
     * @param token Token JWT firmado.
     * @return Nombre de usuario que se guardó como "sub" en el token.
     *
     *         Proceso: 1. Usa la clave secreta para validar y decodificar el token.
     *         2.Extrae el cuerpo (claims) del token.
     *         3. Retorna el claim "subject", que contiene el nombre de usuario.
     */

    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    /**
     * Valida que un token JWT sea correcto.
     *
     * @param token Token JWT firmado.
     * @return true si el token es válido, false si es inválido o ha expirado.
     *
     *         Proceso:
     *         1. Intenta parsear el token usando la clave secreta.
     *         2. Si no hay errores → token válido.
     *         3. Si se lanza alguna excepción → token inválido o caducado.
     *
     */

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException | SignatureException | UnsupportedJwtException | IllegalArgumentException ex) {
            return false;
        }
    }


    public String getRoleFromJWT(String token) {
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return (String) claims.get("role");
    }
}

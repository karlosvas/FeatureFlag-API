package com.equipo01.featureflag.featureflag.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * Utilidad para trabajar con tokens JWT. -Genera tokens JWT firmados con una clave secreta. -Valida
 * tokens JWT y extrae información (como el nombre de usuario).
 *
 * @author alex
 */
@Component
public class JwtUtil {

  // Clave secreta para firmar y validar los tokens JWT
  @Value("${JWT_SECRET_KEY}")
  private String secret;

  @Value("${ACCESS_TOKEN_EXPIRATION}")
  private Long expiration;

  /**
   * Genera un token JWT para un usuario autenticado. -1.Extrae el nombre de usuario del objeto
   * Authentication. -2.Crea un token con el nombre de usuario como subject, la fecha de emisión y
   * expiración. -3.Firma el token con la clave secreta usando HS256. -4.Retorna el token generado.
   *
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
   * Método que extra el nombre de usuario (subject) de un token JWT. -1.Parsea el token usando la
   * clave secreta. -2.Extrae el subject (nombre de usuario) de los claims. -3.Retorna el nombre de
   * usuario.
   *
   * @param token
   * @return
   */
  public String getUsernameFromJWT(String token) {
    Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    return claims.getSubject();
  }

  /**
   * Valida que un token JWT sea correcto. -1. Intenta parsear el token usando la clave secreta. -2.
   * Si el token es válido, retorna true. -3. Si el token es inválido o ha expirado, captura la
   * excepción y retorna false.
   *
   * @param token Token JWT firmado.
   * @return true si el token es válido, false si es inválido o ha expirado.
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

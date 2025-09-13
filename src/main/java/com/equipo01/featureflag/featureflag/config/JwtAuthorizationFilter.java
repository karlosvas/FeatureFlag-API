package com.equipo01.featureflag.featureflag.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Filtro de autorización basado en JWT. -Extiende {@link OncePerRequestFilter} para asegurar que se
 * ejecuta una vez por petición. -Interpreta y valida el token JWT en la cabecera Authorization de
 * las peticiones. -Si el token es válido, extrae el nombre de usuario y establece la autenticación
 * en el contexto de seguridad. -Si el token no es válido o no está presente, la petición continúa
 * sin autorización. -Este filtro se aplica a todas las peticiones protegidas por Spring Security
 * excepto las especificadas.
 *
 * @author alex
 */
public class JwtAuthorizationFilter extends OncePerRequestFilter {

  // Dependencia para trabajar con tokens JWT (generar , validar , extraer datos )
  private final JwtUtil jwtUtil;

  // Dependencia para trabajar con los detalles del usuario
  private final CustomUserDetailsService uds;

  /**
   * Constructor para incializar las dependencias del filtro
   *
   * @param jwtUtil
   */
  public JwtAuthorizationFilter(JwtUtil jwtUtil, CustomUserDetailsService uds) {
    this.jwtUtil = jwtUtil;
    this.uds = uds;
  }

  /**
   * Mètodo que se ejecuta para cada petición HTTP. -1.Obtiene el token JWT de la cabecera
   * Authorization. -2.Valida el token usando {@link JwtUtil}. -3.Si el token es válido, extrae el
   * nombre de usuario y crea un objeto de autenticación. -4.Estalece la autenticación en el
   * contexto de seguridad -5.Continúa con la cadena de filtros.
   *
   * @param request petición HTTP
   * @param response respuesta HTTP
   * @param filterChain cadena de filtros
   * @throws ServletException si ocurre un error en el servlet
   * @throws IOException si ocurre un error de entrada/salida
   */
  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    // 1.Obtenemos el token de la petición
    String token = getJwtFromRequest(request);
    // 2.Verificamos que el token existe y sea válido
    if (token != null && jwtUtil.validateToken(token)) {
      String username = jwtUtil.getUsernameFromJWT(token);
      UserDetails userDetails = uds.loadUserByUsername(username);

      UsernamePasswordAuthenticationToken authentication =
          new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
      authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    // 5.Continúa con la cadena de filtros
    filterChain.doFilter(request, response);
  }

  /**
   * Método que obtiene el token JWT de la cabecera Authorization de la petición HTTP. -1. Obtenemos
   * el contenido de la cabecera Authorization. -2. Verificamos que la cabecera no sea nula y
   * comience con Bearer . -3. Extraemos el token quitando el prefijo Bearer . -4. Si la cabecera no
   * es válida, retornamos null.
   *
   * @param request petición HTTP
   * @return el token JWT sin el prefijo Bearer, o null si no está presente
   */
  private String getJwtFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }
}

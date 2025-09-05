package com.equipo01.featureflag.featureflag.config;

import org.springframework.security.core.context.SecurityContextHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 *
 * @author alex
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // Dependencia para trabajar con tokens JWT (generar , validar , extraer datos )
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetails;

    // Constructor que recibe el JwTokenProvider
    public JwtAuthenticationFilter(JwtUtil jwtUtil, CustomUserDetailsService userDetails) {
        this.jwtUtil = jwtUtil;
        this.userDetails = userDetails;
    }

    /**
     * Mètodo principal del filtro . Se ejecuta una vez por cada petición HTTP
     * debido a que extiende a (OncePerRequestFilter).
     * 
     * @param request     Petición HTTP.
     * @param response    Respuesta HTTP.
     * @param filterChain Cadena de filtros de Spring Security.
     * @throws ServletException
     * @throws IOException
     * 
     *                          Proceso :
     *                          1.Extrae el token JWT en la cabecera Authorization
     *                          2.Si el token existe y es válido -> Obtiene el
     *                          username
     *                          3. (Falta) Crear un objeto Authentiacation y
     *                          guardarlo en SecurityContextHolder.
     *                          4. Continua con la cadena de filtros
     *                          (filterChain.doFilter).
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Paso 1
        String token = getJwtFromRequest(request);
        // Paso 2
        if (token != null && jwtUtil.validateToken(token)) {
            String username = jwtUtil.getUsernameFromJWT(token);
            // Paso 3
            UserDetails uds = userDetails.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(uds,
                    null, uds.getAuthorities());

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);

        }
        filterChain.doFilter(request, response);

    }

    /**
     * Función que captura y devuelve el token JWT enviado en la cabecera de la
     * petición Athorization
     * 
     * @param request
     * @return
     */
    private String getJwtFromRequest(HttpServletRequest request) {

        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
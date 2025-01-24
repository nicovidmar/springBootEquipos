package com.futbol.equipos.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.futbol.equipos.exception.CustomizableException;

import java.io.IOException;

@Component
public class AuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * Filtro de autenticación JWT que intercepta las solicitudes HTTP para validar el token JWT.
     * Este método verifica si la solicitud incluye un encabezado de autorización con un token
     * válido. Si el token es válido, se autentica al usuario configurando el contexto de seguridad.
     * En caso de un token inválido o ausente, se lanza una excepción personalizada.
     *
     * @param request  la solicitud HTTP entrante
     * @param response la respuesta HTTP que se enviará al cliente
     * @param filterChain el conjunto de filtros que forman parte del flujo de procesamiento
     * @throws ServletException en caso de errores relacionados con el procesamiento de la solicitud
     * @throws IOException en caso de errores de entrada/salida
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            try {
                // Validar y obtener claims del token
                Claims claims = jwtUtil.getParser().parseClaimsJws(token).getBody();
                String username = claims.getSubject();

                // Cargar los detalles del usuario
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Configurar la autenticación en el contexto de seguridad
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } catch (AuthenticationException e) {
            // Lanza una excepción personalizada en lugar de escribir directamente la respuesta
            throw new CustomizableException("Debe autenticarse para acceder a este endpoint", 401);
        }
        }

        // Continuar con el filtro
        filterChain.doFilter(request, response);
    }
}

package com.futbol.equipos.security;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configura la cadena de filtros de seguridad para la aplicación.
     * Esta configuración incluye:
     *   Gestión de CORS para permitir solicitudes desde orígenes específicos.
     *   Desactivación de CSRF, ya que la API es stateless.
     *   Autorización de solicitudes: permite acceso público a ciertos endpoints y exige autenticación para el resto.
     *   Establecimiento de una política de sesiones stateless
     *   Incorporación de un filtro personalizado para manejar tokens JWT.
     *   Manejo de excepciones de autenticación mediante un EntryPoint personalizado.
     * 
     * @param http                 Configuración de seguridad de Spring.
     * @param jwtAuthenticationFilter Filtro personalizado para autenticación basada en JWT.
     * @param authEntryPoint       EntryPoint personalizado para manejar errores de autenticación.
     * @return La cadena de filtros configurada.
     * @throws Exception Si ocurre algún error durante la configuración.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthFilter jwtAuthenticationFilter, AuthEntryPoint authEntryPoint) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(request -> {
                    var config = new org.springframework.web.cors.CorsConfiguration();
                    config.setAllowedOrigins(List.of("http://localhost:8088"));
                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    config.setAllowedHeaders(List.of("*")); 
                    config.setAllowCredentials(true);
                    return config;
                }))
                .csrf(csrf -> csrf.disable()) // Deshabilitar CSRF
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll() // permitir sin autenticación
                        .anyRequest().authenticated() // autenticación para el resto
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // Agrega filtro JWT
                .exceptionHandling(ex -> ex.authenticationEntryPoint(authEntryPoint)) // tira excepción si error en autenticación
                .build();
    }

    /**
     * Bean para codificar contraseñas utilizando BCrypt.
     * 
     * @return Un codificador de contraseñas BCrypt.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Bean para gestionar los detalles del usuario en memoria.
     * 
     * @param passwordEncoder El codificador de contraseñas a utilizar.
     * @return Un servicio de detalles de usuario configurado con un usuario en memoria.
     */
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        return new InMemoryUserDetailsManager(
            User.builder()
                .username("test")
                .password(passwordEncoder.encode("12345")) // Contraseña codificada
                .roles("USER")
                .build()
        );
    }

    /**
     * Bean para gestionar la autenticación.
     * 
     * @param authenticationConfiguration Configuración de autenticación proporcionada por Spring Security.
     * @return El administrador de autenticación.
     * @throws Exception Si ocurre un error al obtener el administrador de autenticación.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}

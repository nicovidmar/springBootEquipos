package com.futbol.equipos.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import com.futbol.equipos.request.AuthRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.HashMap;
import java.util.Map;

/* Controlador de Autenticación */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Endpoint para autenticar a un usuario y generar un token JWT.
     * Este método permite al usuario enviar sus credenciales (nombre de usuario y contraseña)
     * para autenticarse. Si las credenciales son correctas, se genera y devuelve un token JWT.
     * En caso de credenciales inválidas, se devuelve una respuesta con código 401 (UNAUTHORIZED).
     *
     * @param authRequest objeto que contiene las credenciales del usuario (nombre de usuario y contraseña)
     * @return ResponseEntity que contiene el token JWT en caso de éxito o un mensaje de error en caso de fallo
     */
    @Operation(summary = "Login", description = "Autenticar user con nombre de usuario y contraseña y generar token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Token JWT generado con éxito"),
        @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    })
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody AuthRequest authRequest) {
        String username = authRequest.getUsername();
        String password = authRequest.getPassword();

        try {
            // autenticar usuario
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            // genera token JWT
            String token = jwtUtil.generateToken(authentication.getName());

            // retorna token generado y respuesta OK 200
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return ResponseEntity.ok(response);

        } catch (AuthenticationException e) {
            // en caso de error en la autenticación, lanza error 401 UNAUTHORIZED y el mensaje.
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Credenciales invalidas");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }
}

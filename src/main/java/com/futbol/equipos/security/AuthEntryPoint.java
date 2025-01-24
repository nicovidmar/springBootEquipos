package com.futbol.equipos.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.futbol.equipos.exception.CustomizableException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/* Bean para validar autenticación */
@Component
public class AuthEntryPoint implements AuthenticationEntryPoint {


    /**
     * Método llamado cuando un usuario no autenticado intenta acceder a un recurso protegido.
     * Envía una respuesta personalizada con un código de estado HTTP 401 (Unauthorized) 
     * y un mensaje indicando que la autenticación es requerida.
     *
     * @param request la solicitud HTTP que provocó la excepción
     * @param response la respuesta HTTP a ser enviada
     * @param authException la excepción de autenticación que describe el problema
     * @throws IOException si ocurre un error al escribir la respuesta
     */
     @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) 
            throws IOException {
        enviarRespuestaPersonalizada(response, 401, "Debe autenticarse para acceder a este endpoint");
    }

    /**
     * Envía una respuesta HTTP personalizada en formato JSON con un código de estado y un mensaje.
     * Este método se utiliza para estructurar las respuestas de error de forma uniforme, 
     * proporcionando un mensaje descriptivo y un código de error correspondiente.
     *
     * @param response la respuesta HTTP en la que se escribirá el contenido
     * @param codigo el código de estado HTTP que se debe establecer en la respuesta
     * @param mensaje el mensaje descriptivo que explica el error
     * @throws IOException si ocurre un error al escribir el contenido en la respuesta
     */
    public void enviarRespuestaPersonalizada(HttpServletResponse response, int codigo, String mensaje) throws IOException {
        response.setContentType("application/json");
        response.setStatus(codigo);

        // Construir respuesta personalizada
        Map<String, Object> body = new HashMap<>();
        body.put("mensaje", mensaje);
        body.put("codigo", codigo);

        // Escribir respuesta JSON
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(body));
    }
}

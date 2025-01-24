package com.futbol.equipos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja excepciones personalizadas lanzadas por la aplicación.
     * 
     * @param ex Excepción del tipo CustomizableException.
     * @return Una respuesta HTTP con el mensaje y el código de error personalizados.
     */
    @ExceptionHandler(CustomizableException.class)
    public ResponseEntity<LinkedHashMap<String, Object>> handleCustomizableException(CustomizableException ex) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        response.put("mensaje", ex.getMensaje());
        response.put("codigo", ex.getCodigo());
        return ResponseEntity.status(ex.getCodigo()).body(response);
    }
    
    /**
     * Maneja excepciones genéricas no controladas.
     * Devuelve un error interno del servidor (HTTP 500) como respuesta.
     * 
     * @param ex Excepción genérica lanzada por la aplicación.
     * @return Una respuesta HTTP con un mensaje estándar de error interno.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<LinkedHashMap<String, Object>> handleGeneralException(Exception ex) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        response.put("mensaje", "Error interno del servidor");
        response.put("codigo", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}


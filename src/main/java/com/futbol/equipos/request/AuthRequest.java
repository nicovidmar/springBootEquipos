package com.futbol.equipos.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request para autenticación")
public class AuthRequest {
    @Schema(description = "Nombre usuario", example = "test")
    private String username;

    @Schema(description = "Contraseña", example = "12345")
    private String password;

    public AuthRequest() {}

    public AuthRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

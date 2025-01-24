package com.futbol.equipos.exception;

public class CustomizableException extends RuntimeException {
    private final int codigo;
    private final String mensaje;

    public CustomizableException(String mensaje, int codigo) {
        super(mensaje);
        this.codigo = codigo;
        this.mensaje = mensaje;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getMensaje() {
        return mensaje;
    }
}

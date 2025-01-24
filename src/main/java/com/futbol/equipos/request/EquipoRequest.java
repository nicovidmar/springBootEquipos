package com.futbol.equipos.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request para la creación o actualización de un equipo")
public class EquipoRequest {

    @Schema(description = "Nombre del equipo", example = "Dux Fc")
    private String nombre;

    @Schema(description = "Liga del equipo", example = "Primera Division")
    private String liga;

    @Schema(description = "País del equipo", example = "Argentina")
    private String pais;

    public EquipoRequest(){}

    public EquipoRequest(String nombre, String liga, String pais) {
        this.nombre = nombre;
        this.liga = liga;
        this.pais = pais;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLiga() {
        return liga;
    }

    public void setLiga(String liga) {
        this.liga = liga;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }
}

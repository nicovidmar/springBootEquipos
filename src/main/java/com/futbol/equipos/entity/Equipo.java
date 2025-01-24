package com.futbol.equipos.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Schema(description = "Entidad que representa un equipo de fútbol")
@Entity
@Table(name = "equipos")
public class Equipo {

    @Schema(description = "ID único del equipo", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Nombre del equipo", example = "Real Madrid")
    private String nombre;

    @Schema(description = "Liga en la que compite el equipo", example = "La Liga")
    private String liga;

    @Schema(description = "País del equipo", example = "España")
    private String pais;

    public Equipo() {}

    public Equipo(Long id, String nombre, String liga, String pais) {
        this.id = id;
        this.nombre = nombre;
        this.liga = liga;
        this.pais = pais;
    }

    public Equipo(String nombre, String liga, String pais) {
        this.nombre = nombre;
        this.liga = liga;
        this.pais = pais;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

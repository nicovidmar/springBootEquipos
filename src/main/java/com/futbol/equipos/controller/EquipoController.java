package com.futbol.equipos.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.futbol.equipos.entity.Equipo;
import com.futbol.equipos.request.EquipoRequest;
import com.futbol.equipos.service.EquipoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/equipos")
public class EquipoController {

    private final EquipoService equipoService;

    public EquipoController(EquipoService equipoService) {
        this.equipoService = equipoService;
    }

    /**
     * Obtiene una lista de todos los equipos registrados.
     *
     * @return Lista de equipos en formato JSON.
     */
    @Operation(summary = "Obtener todos los equipos", description = "Devuelve una lista de todos los equipos registrados.")
    @ApiResponse(responseCode = "200", description = "Lista de equipos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Equipo.class)))
    @GetMapping
    public List<Equipo> getAllEquipos() {
        return equipoService.findAll();
    }

    /**
     * Obtiene un equipo específico según su ID.
     *
     * @param id ID del equipo a buscar.
     * @return El equipo encontrado o un error 404 si no existe.
     */
    @Operation(summary = "Obtener un equipo por ID", description = "Devuelve un equipo según su ID.")
    @ApiResponse(responseCode = "200", description = "Equipo encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Equipo.class)))
    @ApiResponse(responseCode = "404", description = "Equipo no encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<?> getEquipoById(@Parameter(description = "ID del equipo", example = "1") @PathVariable Long id) {
        Equipo equipo = equipoService.findById(id);
        return ResponseEntity.ok(equipo);
    }

    /**
     * Busca equipos cuyo nombre contenga el texto proporcionado.
     *
     * @param nombre Texto parcial o completo a buscar.
     * @return Lista de equipos encontrados o un error 404 si no se encuentran coincidencias.
     */
    @Operation(summary = "Buscar equipos por nombre", description = "Busca equipos cuyo nombre contenga el texto proporcionado.")
    @ApiResponse(responseCode = "200", description = "Equipos encontrados", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Equipo.class)))
    @ApiResponse(responseCode = "404", description = "No se encontraron equipos con ese nombre")
    @GetMapping("/buscar")
    public ResponseEntity<?> buscarEquiposPorNombre(
            @Parameter(description = "Nombre parcial o completo del equipo", example = "Real") @RequestParam String nombre) {
        List<Equipo> equipos = equipoService.findAllByNombreContaining(nombre);
        
        return ResponseEntity.ok(equipos);
    }

    /**
     * Crea un nuevo equipo con los datos proporcionados.
     *
     * @param equipoRequest Objeto que contiene los datos del equipo a crear.
     * @return El equipo creado con un estado HTTP 201.
     */
    @Operation(summary = "Crear un nuevo equipo", description = "Crea un equipo nuevo con los datos proporcionados.")
    @ApiResponse(responseCode = "201", description = "Equipo creado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Equipo.class)))
    @RequestBody(description = "Datos del equipo a crear", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = EquipoRequest.class)))
    @PostMapping
    public ResponseEntity<?> createEquipo(@org.springframework.web.bind.annotation.RequestBody EquipoRequest equipoRequest) {
        equipoService.validarRequest(equipoRequest);
        Equipo savedEquipo = equipoService.save(convertirRequestAEquipo(equipoRequest));
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEquipo);
    }

    /**
     * Actualiza los datos de un equipo existente basado en su ID.
     *
     * @param id ID del equipo a actualizar.
     * @param equipoRequest Datos actualizados del equipo.
     * @return El equipo actualizado o un error 404 si no existe.
     */
    @Operation(summary = "Actualizar un equipo por ID", description = "Actualiza los datos de un equipo existente.")
    @ApiResponse(responseCode = "200", description = "Equipo actualizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Equipo.class)))
    @ApiResponse(responseCode = "404", description = "Equipo no encontrado")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEquipo(
            @Parameter(description = "ID del equipo", example = "1") @PathVariable Long id, 
            @org.springframework.web.bind.annotation.RequestBody EquipoRequest equipoRequest) {

        Equipo equipoActualizado = equipoService.updateEquipo(id, equipoRequest);

        return ResponseEntity.ok(equipoActualizado);
    }

    /**
     * Elimina un equipo de la base de datos según su ID.
     *
     * @param id ID del equipo a eliminar.
     * @return Respuesta sin contenido con estado HTTP 204.
     */
    @Operation(summary = "Eliminar un equipo por ID", description = "Elimina un equipo basado en su ID.")
    @ApiResponse(responseCode = "204", description = "Equipo eliminado")
    @ApiResponse(responseCode = "404", description = "Equipo no encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEquipo(@Parameter(description = "ID del equipo", example = "1") @PathVariable Long id) {
        equipoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Convierte un objeto EquipoRequest en un objeto Equipo.
     *
     * @param equipoRequest El objeto EquipoRequest a convertir.
     * @return Un objeto Equipo con los datos del request.
     */
    private Equipo convertirRequestAEquipo(EquipoRequest equipoRequest) {
        Equipo equipo = new Equipo();
        equipo.setNombre(equipoRequest.getNombre());
        equipo.setLiga(equipoRequest.getLiga());
        equipo.setPais(equipoRequest.getPais());
        return equipo;
    }
}

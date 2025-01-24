package com.futbol.equipos.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.futbol.equipos.entity.Equipo;
import com.futbol.equipos.exception.CustomizableException;
import com.futbol.equipos.repository.EquipoRepository;
import com.futbol.equipos.request.EquipoRequest;

@Service
public class EquipoService {

    private final EquipoRepository equipoRepository;

    public static Set<String> parametrosEsperados = Set.of("nombre", "liga", "pais");

    public EquipoService(EquipoRepository equipoRepository) {
        this.equipoRepository = equipoRepository;
    }

    /**
     * Obtiene todos los equipos de la base de datos.
     *
     * @return Lista de todos los equipos.
     */
    public List<Equipo> findAll() {
        return equipoRepository.findAll();
    }

    /**
     * Busca un equipo por su ID.
     *
     * @param id El ID del equipo a buscar.
     * @return El equipo encontrado.
     * @throws CustomizableException Si el equipo no existe.
     */
    public Equipo findById(Long id) {
        return equipoRepository.findById(id)
                .orElseThrow(() -> new CustomizableException("Equipo no encontrado.", 404));
    }

    /**
     * Guarda un nuevo equipo en la base de datos.
     *
     * @param equipo El equipo a guardar.
     * @return El equipo guardado.
     */
    public Equipo save(Equipo equipo) {
        return equipoRepository.save(equipo);
    }

    /**
     * Elimina un equipo de la base de datos por su ID.
     *
     * @param id El ID del equipo a eliminar.
     */
    public void deleteById(Long id) {
        equipoRepository.deleteById(id);
    }

    /**
     * Actualiza un equipo existente en la base de datos.
     *
     * @param id El ID del equipo a actualizar.
     * @param equipoRequest Los datos actualizados del equipo.
     * @return El equipo actualizado.
     * @throws CustomizableException Si el equipo no existe o los datos son inválidos.
     */
    public Equipo updateEquipo(Long id, EquipoRequest equipoRequest) {
        // Validar los campos del request
        validarRequest(equipoRequest);
    
        // Intentar obtener el equipo o lanzar una excepción si no existe
        Equipo equipoAActualizar = equipoRepository.findById(id)
                .orElseThrow(() -> new CustomizableException("Equipo no encontrado.", 404));
    
        // Actualizar los valores del equipo
        equipoAActualizar.setNombre(equipoRequest.getNombre());
        equipoAActualizar.setLiga(equipoRequest.getLiga());
        equipoAActualizar.setPais(equipoRequest.getPais());
    
        // Guardar los cambios y devolver el equipo actualizado
        return equipoRepository.save(equipoAActualizar);
    }

    /**
     * Busca equipos cuyo nombre contenga una cadena de String específica sin importar mayúsculas/minúsculas.
     *
     * @param nombre La cadena a buscar en los nombres de los equipos.
     * @return Lista de equipos cuyo nombre contenga la cadena especificada.
     * @throws CustomizableException Si no se encuentran equipos.
     */
    public List<Equipo> findAllByNombreContaining(String nombre) {
        List<Equipo> equipos = equipoRepository.findAllByNombreContainingIgnoreCase(nombre);
        if (equipos.isEmpty()) {
            throw new CustomizableException("Equipo no encontrado.", 404);
        }
        return equipos;
    }

    /**
     * Verifica si los campos de un EquipoRequest están vacíos o son nulos.
     *
     * @param equipoRequest El objeto EquipoRequest a validar.
     * @return true si algún campo está vacío o es nulo; de lo contrario, false,
     */
    public boolean tieneCamposVacios(EquipoRequest equipoRequest) {
        return equipoRequest.getNombre() == null || equipoRequest.getNombre().trim().isEmpty() ||
            equipoRequest.getLiga() == null || equipoRequest.getLiga().trim().isEmpty() ||
            equipoRequest.getPais() == null || equipoRequest.getPais().trim().isEmpty();
    }

     /**
     * Valida los campos de un EquipoRequest.
     * Lanza una excepción si algún campo está vacío o es nulo.
     *
     * @param equipoRequest El objeto EquipoRequest a validar.
     * @throws CustomizableException Si algún campo es inválido.
     */
    public void validarRequest(EquipoRequest equipoRequest) {
        if (tieneCamposVacios(equipoRequest)) {
            throw new CustomizableException("La solicitud es invalida", 400);
        }
    }

}


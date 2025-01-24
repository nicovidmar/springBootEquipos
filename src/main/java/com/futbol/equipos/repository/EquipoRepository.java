package com.futbol.equipos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.futbol.equipos.entity.Equipo;

@Repository
public interface EquipoRepository extends JpaRepository<Equipo, Long> {

    List<Equipo> findAllByNombreContainingIgnoreCase(String nombre);
}

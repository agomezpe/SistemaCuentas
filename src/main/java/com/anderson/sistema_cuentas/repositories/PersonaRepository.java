package com.anderson.sistema_cuentas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anderson.sistema_cuentas.entities.Persona;

public interface PersonaRepository extends JpaRepository<Persona, Long> {
	boolean existsByIdentificacion(String identificacion);
}

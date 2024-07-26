package com.anderson.sistema_cuentas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anderson.sistema_cuentas.entities.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    // Puedes añadir métodos de consulta personalizados aquí si es necesario
}

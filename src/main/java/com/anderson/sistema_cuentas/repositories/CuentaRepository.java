package com.anderson.sistema_cuentas.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anderson.sistema_cuentas.entities.Cliente;
import com.anderson.sistema_cuentas.entities.Cuenta;

public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
	
	boolean existsByNumeroCuentaAndCliente(Long numeroCuenta, Cliente cliente);
	
	boolean existsByCliente_ClienteId(Long clienteId);
	
	boolean existsByNumeroCuenta(Long numeroCuenta);
	
	Cuenta findByNumeroCuenta(Long numeroCuenta);
	
	List<Cuenta> findByCliente_ClienteId(Long clienteId);
}

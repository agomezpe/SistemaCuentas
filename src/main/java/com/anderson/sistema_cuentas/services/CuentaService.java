package com.anderson.sistema_cuentas.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anderson.sistema_cuentas.entities.Cuenta;
import com.anderson.sistema_cuentas.exception.CuentaNotFoundException;
import com.anderson.sistema_cuentas.exception.InsertException;
import com.anderson.sistema_cuentas.repositories.CuentaRepository;

@Service
public class CuentaService {

    @Autowired
    private CuentaRepository cuentaRepository;

    public List<Cuenta> findAll() {
        return cuentaRepository.findAll();
    }

    public Optional<Cuenta> findById(Long numeroCuenta) {
    	return Optional.ofNullable(cuentaRepository.findById(numeroCuenta)
                .orElseThrow(() -> new CuentaNotFoundException("Cuenta con número " + numeroCuenta + " no encontrada")));
    }

    public Cuenta save(Cuenta cuenta, boolean isNewOne) {
        if (isNewOne) {
	    	// Verificar existencia de cuenta con el mismo número para el cliente
	        boolean exists = cuentaRepository.existsByNumeroCuentaAndCliente(cuenta.getNumeroCuenta(), cuenta.getCliente());
	        if (exists) {
	            throw new InsertException("Ya existe una cuenta con el número " + cuenta.getNumeroCuenta() + " para el cliente.");
	        }
        }
        return cuentaRepository.save(cuenta);
    }

    public void deleteById(Long id) {
        if (cuentaRepository.existsById(id)) {
            cuentaRepository.deleteById(id);
        } else {
            throw new CuentaNotFoundException("Cuenta con número " + id + " no encontrada");
        }
    }
}

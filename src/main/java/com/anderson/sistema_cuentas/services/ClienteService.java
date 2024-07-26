package com.anderson.sistema_cuentas.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anderson.sistema_cuentas.entities.Cliente;
import com.anderson.sistema_cuentas.entities.Persona;
import com.anderson.sistema_cuentas.exception.ClientHasAccountsException;
import com.anderson.sistema_cuentas.exception.ClienteNotFoundException;
import com.anderson.sistema_cuentas.exception.InsertException;
import com.anderson.sistema_cuentas.repositories.ClienteRepository;
import com.anderson.sistema_cuentas.repositories.CuentaRepository;
import com.anderson.sistema_cuentas.repositories.PersonaRepository;

import jakarta.transaction.Transactional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private PersonaRepository personaRepository;
    
	@Autowired
    private CuentaRepository cuentaRepository;

    public List<Cliente> findAll() {
        List<Cliente> clientes = clienteRepository.findAll();
        if (clientes.isEmpty()) {
            throw new ClienteNotFoundException("No se encontraron clientes.");
        }
        return clientes;
    }

    public Optional<Cliente> findById(Long id) {
    	if (clienteRepository.existsById(id)) {
    		clienteRepository.findById(id);
    		return clienteRepository.findById(id);
    	} else {
            throw new ClienteNotFoundException("Cliente con ID " + id + " no encontrado");
        }
        
    }

    public Cliente save(Cliente cliente, boolean isNewOne) {

        // Guardar primero la persona
	    if (personaRepository.existsByIdentificacion(cliente.getPersona().getIdentificacion()) && isNewOne) {
	        throw new InsertException("La identificación ya está en uso: " + cliente.getPersona().getIdentificacion());
	    }
        Persona savedPersona = personaRepository.save(cliente.getPersona());
        cliente.setPersona(savedPersona);
        return clienteRepository.save(cliente);

    }

    @Transactional
    public void deleteById(Long id) {
        // Verificar si el cliente existe
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException("Cliente con ID " + id + " no encontrado."));

        // Verificar si el cliente tiene cuentas asociadas
        if (cuentaRepository.existsByCliente_ClienteId(id)) {
            throw new ClientHasAccountsException("Cliente con ID " + id + " tiene cuentas asociadas.");
        }

        // Eliminar la persona asociada
        Persona persona = cliente.getPersona();
        if (persona != null) {
            // Eliminar el cliente
            clienteRepository.deleteById(id);
            personaRepository.deleteById(persona.getId());
        }


    }

}

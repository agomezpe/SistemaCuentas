package com.anderson.sistema_cuentas.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anderson.sistema_cuentas.entities.Persona;
import com.anderson.sistema_cuentas.repositories.PersonaRepository;

@Service
public class PersonaService {
	
    @Autowired
    private PersonaRepository personaRepository;
	
    public Persona save(Persona persona) {
        return personaRepository.save(persona);
    }
    
    public Optional<Persona> findById(Long id) {
        return personaRepository.findById(id);
    }

    public void deleteById(Long id) {
        personaRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return personaRepository.existsById(id);
    }

}

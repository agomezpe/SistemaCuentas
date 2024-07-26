package com.anderson.sistema_cuentas.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anderson.sistema_cuentas.dto.ClienteDTO;
import com.anderson.sistema_cuentas.dto.UpdateClienteDTO;
import com.anderson.sistema_cuentas.entities.Cliente;
import com.anderson.sistema_cuentas.entities.Persona;
import com.anderson.sistema_cuentas.exception.ClientHasAccountsException;
import com.anderson.sistema_cuentas.exception.ClienteNotFoundException;
import com.anderson.sistema_cuentas.exception.InsertException;
import com.anderson.sistema_cuentas.services.ClienteService;
import com.anderson.sistema_cuentas.services.PersonaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

	@Autowired
    private ClienteService clienteService;
	
	@Autowired
	private PersonaService personaService;
	
    @Autowired
    private ModelMapper modelMapper;
    
    private static final Logger logger = LoggerFactory.getLogger(ClienteController.class);

    @GetMapping
    public List<ClienteDTO> getAllClientes() {
        return clienteService.findAll().stream()
                .map(cliente -> modelMapper.map(cliente, ClienteDTO.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> getClienteById(@PathVariable Long id) {
        Optional<Cliente> cliente = clienteService.findById(id);
        return new ResponseEntity<>(modelMapper.map(cliente, ClienteDTO.class), HttpStatus.OK);
    }
    
    @PostMapping
    public ResponseEntity<Object> createCliente(@Valid @RequestBody ClienteDTO clienteDTO, 
    		BindingResult bindingResult) {
    	
        // Log de depuración para los parámetros de entrada
        logger.debug("Request received to createCliente with ClienteDTO: {}", clienteDTO);
        logger.debug("BindingResult: {}", bindingResult);
        
        // Verificar si hay errores de validación
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                .collect(Collectors.toMap(
                    FieldError::getField,
                    FieldError::getDefaultMessage
                ));
            logger.warn("Validation errors: {}", errors);
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
    	
    	try {
            // Convertir DTO a entidad Cliente
            Cliente cliente = modelMapper.map(clienteDTO, Cliente.class);
            
            // Crear la Persona desde el DTO
            Persona persona = new Persona();
            persona.setNombre(clienteDTO.getNombre());
            persona.setGenero(clienteDTO.getGenero());
            persona.setEdad(clienteDTO.getEdad());
            persona.setIdentificacion(clienteDTO.getIdentificacion());
            persona.setDireccion(clienteDTO.getDireccion());
            persona.setTelefono(clienteDTO.getTelefono());

            // Asignar la Persona al Cliente
            cliente.setPersona(persona);

            // Guardar el Cliente (esto guardará también la Persona asociada)
            Cliente nuevoCliente = clienteService.save(cliente, true);

            // Convertir la entidad guardada a DTO
            ClienteDTO nuevoClienteDTO = modelMapper.map(nuevoCliente, ClienteDTO.class);

            logger.info("Cliente creado satsisfactoriamente con el ID: {}", nuevoClienteDTO.getClienteId());
            return new ResponseEntity<>(nuevoClienteDTO, HttpStatus.CREATED);
        } catch (InsertException ie) {
        	logger.error("La identificación ya está en uso: " + clienteDTO.getIdentificacion());
        	return new ResponseEntity<>(ie.getMessage(), HttpStatus.CONFLICT);
		} catch (Exception e) {
        	logger.error("Error occurred while creating cliente", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCliente(@PathVariable Long id, 
    		@Valid @RequestBody UpdateClienteDTO updateClienteDTO, 
    		BindingResult bindingResult) {
    	
        // Verificar si hay errores de validación
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                .collect(Collectors.toMap(
                    FieldError::getField,
                    FieldError::getDefaultMessage
                ));
            logger.warn("Validation errors: {}", errors);
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        try {
            Optional<Cliente> existingClienteOpt = clienteService.findById(id);

            if (existingClienteOpt.isPresent()) {
                Cliente existingCliente = existingClienteOpt.get();

                // Actualizar campos del cliente
                if (updateClienteDTO.getContrasena() != null) {
                    existingCliente.setContrasena(updateClienteDTO.getContrasena());
                }
                if (updateClienteDTO.getEstado() != null) {
                    existingCliente.setEstado(updateClienteDTO.getEstado());
                }

                Persona existingPersona = existingCliente.getPersona();
                
                // Actualizar campos de la persona
                if (updateClienteDTO.getNombre() != null) {
                    existingPersona.setNombre(updateClienteDTO.getNombre());
                }
                if (updateClienteDTO.getGenero() != null) {
                    existingPersona.setGenero(updateClienteDTO.getGenero());
                }
                if (updateClienteDTO.getEdad() != null) {
                    existingPersona.setEdad(updateClienteDTO.getEdad());
                }
                if (updateClienteDTO.getIdentificacion() != null) {
                    existingPersona.setIdentificacion(updateClienteDTO.getIdentificacion());
                }
                if (updateClienteDTO.getDireccion() != null) {
                    existingPersona.setDireccion(updateClienteDTO.getDireccion());
                }
                if (updateClienteDTO.getTelefono() != null) {
                    existingPersona.setTelefono(updateClienteDTO.getTelefono());
                }

                // Guardar cambios
                personaService.save(existingPersona);
                clienteService.save(existingCliente, false);

                // Mapear a DTO para la respuesta
                ClienteDTO updatedClienteDTO = modelMapper.map(existingCliente, ClienteDTO.class);
                logger.info("Cliente actualizado satsisfactoriamente con el ID: {}", updatedClienteDTO.getClienteId());
                return new ResponseEntity<>(updatedClienteDTO, HttpStatus.OK);
            } else {
            	logger.info("Cliente no encontrado con el ID: {}", id);
                return new ResponseEntity<>("Cliente no encontrado con el ID: " + id, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
        	logger.error("Error occurred while updated cliente", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCliente(@PathVariable Long id) {
    	try {
	    	clienteService.deleteById(id);
	        logger.info("El cliente con el ID: " + id + " se elimino satisfactoriamente");
	        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ClientHasAccountsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (ClienteNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}

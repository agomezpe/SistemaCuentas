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

import com.anderson.sistema_cuentas.dto.ClienteCuentaResponseDTO;
import com.anderson.sistema_cuentas.dto.CuentaDTO;
import com.anderson.sistema_cuentas.dto.UpdateCuentaDTO;
import com.anderson.sistema_cuentas.entities.Cliente;
import com.anderson.sistema_cuentas.entities.Cuenta;
import com.anderson.sistema_cuentas.exception.InsertException;
import com.anderson.sistema_cuentas.repositories.ClienteRepository;
import com.anderson.sistema_cuentas.services.CuentaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/cuentas")
public class CuentaController {

	@Autowired
    private CuentaService cuentaService;
	
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ModelMapper modelMapper;
    
    private static final Logger logger = LoggerFactory.getLogger(CuentaController.class);

    @GetMapping
    public List<CuentaDTO> getAllCuentas() {
        return cuentaService.findAll().stream()
                .map(cuenta -> modelMapper.map(cuenta, CuentaDTO.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{numeroCuenta}")
    public ResponseEntity<CuentaDTO> getCuentaById(@PathVariable Long numeroCuenta) {
        Optional<Cuenta> cuenta = cuentaService.findById(numeroCuenta);
        return new ResponseEntity<>(modelMapper.map(cuenta, CuentaDTO.class), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> createCuenta(@Valid @RequestBody CuentaDTO cuentaDTO, 
    		BindingResult bindingResult) {
    	
        // Log de depuración para los parámetros de entrada
        logger.debug("Request received to createCuenta with CuentaDTO: {}", cuentaDTO);
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
        	if (clienteRepository.existsById(cuentaDTO.getClienteId())) {
        		Cliente cliente = clienteRepository.findById(cuentaDTO.getClienteId()).orElse(null);
        		
        		
		        Cuenta cuenta = modelMapper.map(cuentaDTO, Cuenta.class);
		        Cuenta createdCuenta = cuentaService.save(cuenta, true);
		        
		        ClienteCuentaResponseDTO responseDTO = setResponseCreateAccount(cliente, createdCuenta);
		        
		        logger.info("Cuenta creada satsisfactoriamente con el ID: {}", createdCuenta.getNumeroCuenta());
		        return new ResponseEntity<>(modelMapper.map(responseDTO, ClienteCuentaResponseDTO.class), HttpStatus.CREATED);
        	} else {
	        	logger.info("Cliente no encontrado con el ID: {}", cuentaDTO.getClienteId());
                return new ResponseEntity<>("Cliente no encontrado con el ID: " + cuentaDTO.getClienteId(), HttpStatus.NOT_FOUND);
	        }
        } catch (InsertException e) {
        	logger.error("La cuenta ya existe para el cliente", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT); 
        } catch (Exception e) {
        	logger.error("Error occurred while creating account", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateCuenta(@PathVariable Long id,
                                               @Valid @RequestBody UpdateCuentaDTO updateCuentaDTO,
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
        	Optional<Cuenta> existingCuentaOpt = cuentaService.findById(id);
           
            if (existingCuentaOpt.isPresent()) {
                Cuenta existingCuenta = existingCuentaOpt.get();
                
                if (existingCuenta != null) {
                    // Actualizar solo los campos no nulos
                    if (updateCuentaDTO.getTipoCuenta() != null) {
                        existingCuenta.setTipoCuenta(updateCuentaDTO.getTipoCuenta());
                    }
                    if (updateCuentaDTO.isEstado()) {
                        existingCuenta.setEstado(updateCuentaDTO.isEstado());
                    }

                    // Guardar la cuenta actualizada
                    Cuenta updatedCuenta = cuentaService.save(existingCuenta,false);

                    // Obtener el cliente para la respuesta
                    Cliente cliente = clienteRepository.findById(existingCuenta.getCliente().getClienteId()).orElse(null);

                    // Crear DTO de respuesta
                    ClienteCuentaResponseDTO responseDTO = setResponseCreateAccount(cliente, updatedCuenta);

                    logger.info("Cuenta actualizada satisfactoriamente con el ID: {}", updatedCuenta.getNumeroCuenta());
                    return new ResponseEntity<>(responseDTO, HttpStatus.OK);
                } else {
                    logger.info("Cuenta no encontrada con el ID: {}", id);
                    return new ResponseEntity<>("Cuenta no encontrada con el ID: " + id, HttpStatus.NOT_FOUND);
                }
            } else {
                logger.info("Cuenta no encontrada con el ID: {}", id);
                return new ResponseEntity<>("Cuenta no encontrada con el ID: " + id, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("Error occurred while updating account", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCuenta(@PathVariable Long id) {
        cuentaService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
	private ClienteCuentaResponseDTO setResponseCreateAccount(Cliente cliente, Cuenta createdCuenta) {
		ClienteCuentaResponseDTO responseDTO = new ClienteCuentaResponseDTO();
		responseDTO.setNumeroCuenta(createdCuenta.getNumeroCuenta());
		responseDTO.setTipoCuenta(createdCuenta.getTipoCuenta());
		responseDTO.setSaldoInicial(createdCuenta.getSaldoInicial());
		responseDTO.setEstado(createdCuenta.isEstado());
		responseDTO.setNombreCliente(cliente.getPersona().getNombre());
		return responseDTO;
	}
}

package com.anderson.sistema_cuentas.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import com.anderson.sistema_cuentas.dto.MovimientoDTO;
import com.anderson.sistema_cuentas.dto.MovimientoResponseDTO;
import com.anderson.sistema_cuentas.entities.Movimiento;
import com.anderson.sistema_cuentas.services.MovimientoService;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/movimientos")
public class MovimientoController {

	@Autowired
    private MovimientoService movimientoService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public List<MovimientoDTO> getAllMovimientos() {
        return movimientoService.findAll().stream()
                .map(movimiento -> modelMapper.map(movimiento, MovimientoDTO.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovimientoDTO> getMovimientoById(@PathVariable Long id) {
        Optional<Movimiento> movimiento = movimientoService.findById(id);
        return new ResponseEntity<>(modelMapper.map(movimiento, MovimientoDTO.class), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> createMovimiento(@Valid @RequestBody MovimientoDTO movimientoDTO, BindingResult bindingResult) {
    	
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                .collect(Collectors.toMap(
                    FieldError::getField,
                    FieldError::getDefaultMessage
                ));
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        
        Movimiento movimiento = modelMapper.map(movimientoDTO, Movimiento.class);
        MovimientoResponseDTO createdMovimiento = movimientoService.save(movimiento);
        return new ResponseEntity<>(modelMapper.map(createdMovimiento, MovimientoResponseDTO.class), HttpStatus.CREATED);
    }

}

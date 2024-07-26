package com.anderson.sistema_cuentas.controllers;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anderson.sistema_cuentas.dto.EstadoCuentaDTO;
import com.anderson.sistema_cuentas.services.MovimientoService;

@RestController
@RequestMapping("/reportes")
public class ReporteController {
	
    @Autowired
    private MovimientoService movimientoService;

    @GetMapping
    public ResponseEntity<EstadoCuentaDTO> obtenerReporte(
            @RequestParam Long clienteId,
            @RequestParam LocalDate fechaInicio,
            @RequestParam LocalDate fechaFin) {

        EstadoCuentaDTO reporte = movimientoService.generarReporte(clienteId, fechaInicio, fechaFin);
        return ResponseEntity.ok(reporte);
    }

}

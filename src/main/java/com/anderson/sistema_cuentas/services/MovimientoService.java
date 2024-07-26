package com.anderson.sistema_cuentas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.anderson.sistema_cuentas.config.BigDecimalWrapper;
import com.anderson.sistema_cuentas.dto.CuentaReporteDTO;
import com.anderson.sistema_cuentas.dto.EstadoCuentaDTO;
import com.anderson.sistema_cuentas.dto.MovimientoDTO;
import com.anderson.sistema_cuentas.dto.MovimientoResponseDTO;
import com.anderson.sistema_cuentas.entities.Cuenta;
import com.anderson.sistema_cuentas.entities.Movimiento;
import com.anderson.sistema_cuentas.enums.TipoMovimiento;
import com.anderson.sistema_cuentas.exception.CuentaNotFoundException;
import com.anderson.sistema_cuentas.exception.DailyLimitExceededException;
import com.anderson.sistema_cuentas.exception.InsufficientFundsException;
import com.anderson.sistema_cuentas.exception.MovimientoNotFoundException;
import com.anderson.sistema_cuentas.repositories.CuentaRepository;
import com.anderson.sistema_cuentas.repositories.MovimientoRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovimientoService {

    @Autowired
    private MovimientoRepository movimientoRepository;
    
    @Autowired
    private CuentaRepository cuentaRepository;

    public List<Movimiento> findAll() {
        return movimientoRepository.findAll();
    }

    public Optional<Movimiento> findById(Long id) {
    	 return Optional.ofNullable(movimientoRepository.findById(id)
                 .orElseThrow(() -> new MovimientoNotFoundException("Movimiento con ID " + id + " no encontrado")));
    }

    public MovimientoResponseDTO  save(Movimiento movimiento) {
    	Long numeroCuenta = movimiento.getCuenta().getNumeroCuenta();
    	Cuenta cuenta = cuentaRepository.findByNumeroCuenta(numeroCuenta);
        // Verificar si la cuenta existe
    	if (cuenta == null) {
            throw new CuentaNotFoundException("La cuenta con número " + numeroCuenta + " no existe.");
        }
    	// Validar y actualizar el saldo
    	BigDecimal saldoUltimoMovimiento = getSaldoDelUltimoMovimiento(cuenta.getId());
    	BigDecimal saldoActual = getSaldoActual(cuenta, saldoUltimoMovimiento);
        BigDecimal nuevoSaldo = calcularNuevoSaldo(saldoActual, movimiento.getValor(), movimiento.getTipoMovimiento());
        
        // Verificar si el nuevo saldo es menor que cero
        if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientFundsException("Saldo no disponible para realizar el débito.");
        }
        
        // Verificar el límite diario de retiro si el tipo de movimiento es RETIRO
        if (movimiento.getTipoMovimiento().equals(TipoMovimiento.RETIRO)) {
            BigDecimal limiteDiarioRetiro = cuenta.getLimiteDiarioRetiro();
            if (limiteDiarioRetiro == null) {
                limiteDiarioRetiro = BigDecimal.valueOf(1000); // Valor predeterminado
            }
            BigDecimal totalRetiradoHoy = movimientoRepository.sumValoresByNumeroCuentaAndFecha(
            		cuenta.getId(), LocalDate.now());
            BigDecimal cupoDisponible = limiteDiarioRetiro.subtract(totalRetiradoHoy);

            if (movimiento.getValor().compareTo(cupoDisponible) > 0) {
                throw new DailyLimitExceededException("Cupo diario excedido. Solo se permite retirar hasta " + cupoDisponible);
            }
        }
        
        // Actualizar el saldo de la cuenta
        movimiento.setCuenta(cuenta);
        
        movimiento.setSaldo(nuevoSaldo);
        Movimiento savedMovimiento = movimientoRepository.save(movimiento);
        
        return setResponseCreatedDTO(cuenta, savedMovimiento);
    }
    
    public EstadoCuentaDTO generarReporte(Long clienteId, LocalDate fechaInicio, LocalDate fechaFin) {
        // Obtener todas las cuentas del cliente
        List<Cuenta> cuentas = cuentaRepository.findByCliente_ClienteId(clienteId);

        // Inicializar los totales
        final BigDecimalWrapper totalDebitos = new BigDecimalWrapper(BigDecimal.ZERO);
        final BigDecimalWrapper totalCreditos = new BigDecimalWrapper(BigDecimal.ZERO);

        // Crear una lista para los detalles de cuentas
        List<CuentaReporteDTO> cuentaReportes = cuentas.stream()
            .map(cuenta -> {
                // Calcular totales para cada cuenta
                BigDecimal totalDebitosCuenta = movimientoRepository.sumValoresByTipoMovimientoAndFecha(
                    cuenta.getId(), TipoMovimiento.RETIRO, fechaInicio, fechaFin);
                BigDecimal totalCreditosCuenta = movimientoRepository.sumValoresByTipoMovimientoAndFecha(
                    cuenta.getId(), TipoMovimiento.DEPOSITO, fechaInicio, fechaFin);
                BigDecimal saldoCuenta = cuenta.getSaldoInicial()
                        .subtract(totalDebitosCuenta)
                        .add(totalCreditosCuenta);

                // Acumular totales generales
                totalDebitos.setValue(totalDebitos.getValue().add(totalDebitosCuenta));
                totalCreditos.setValue(totalCreditos.getValue().add(totalCreditosCuenta));

                // Construir el DTO para la cuenta
                CuentaReporteDTO cuentaReporte = new CuentaReporteDTO();
                cuentaReporte.setNumeroCuenta(cuenta.getNumeroCuenta());
                cuentaReporte.setTipoCuenta(cuenta.getTipoCuenta());
                cuentaReporte.setSaldoInicial(cuenta.getSaldoInicial());
                cuentaReporte.setEstadoCuenta(cuenta.isEstado());
                cuentaReporte.setSaldo(saldoCuenta);
                cuentaReporte.setTotalDebitos(totalDebitosCuenta);
                cuentaReporte.setTotalCreditos(totalCreditosCuenta);
                
             // Agregar la fecha de los movimientos al reporte de la cuenta
                List<Movimiento> movimientos = movimientoRepository.findByCuentaIdAndFechaBetween(
                        cuenta.getId(), fechaInicio, fechaFin);
                List<MovimientoDTO> movimientosDTO = movimientos.stream().map(movimiento -> {
                    MovimientoDTO movimientoDTO = new MovimientoDTO();
                    movimientoDTO.setFecha(movimiento.getFecha());
                    movimientoDTO.setValor(movimiento.getValor());
                    movimientoDTO.setTipoMovimiento(movimiento.getTipoMovimiento());
                    return movimientoDTO;
                }).collect(Collectors.toList());
                
                cuentaReporte.setMovimientos(movimientosDTO);

                return cuentaReporte;
            })
            .collect(Collectors.toList());

        // Construir el DTO de respuesta
        EstadoCuentaDTO estadoCuentaDTO = new EstadoCuentaDTO();
        estadoCuentaDTO.setClienteId(clienteId);
        estadoCuentaDTO.setCuentas(cuentaReportes);
        estadoCuentaDTO.setTotalDebitos(totalDebitos.getValue());
        estadoCuentaDTO.setTotalCreditos(totalCreditos.getValue());

        return estadoCuentaDTO;
    }

	private BigDecimal getSaldoActual(Cuenta cuenta, BigDecimal saldoUltimoMovimiento) {
		return (saldoUltimoMovimiento != null && saldoUltimoMovimiento.compareTo(BigDecimal.ZERO) != 0) 
    	    ? saldoUltimoMovimiento 
    	    : cuenta.getSaldoInicial();
	}

    
    private BigDecimal calcularNuevoSaldo(BigDecimal saldoActual, BigDecimal valor, TipoMovimiento tipoMovimiento) {
        if (tipoMovimiento == TipoMovimiento.DEPOSITO) {
            return saldoActual.add(valor);
        } else if (tipoMovimiento == TipoMovimiento.RETIRO) {
            return saldoActual.subtract(valor);
        }
        throw new IllegalArgumentException("Tipo de movimiento inválido");
    }
    
    public BigDecimal getSaldoDelUltimoMovimiento(Long cuentaId) {
        Pageable pageable = PageRequest.of(0, 1); // Solo queremos el último movimiento
        List<Movimiento> movimientos = movimientoRepository.findLatestMovimientoByCuentaId(cuentaId, pageable);


        if (!movimientos.isEmpty()) {
            return movimientos.get(0).getSaldo();
        } else {
            return BigDecimal.ZERO;
        }

    }
    
	private MovimientoResponseDTO setResponseCreatedDTO(Cuenta cuenta, Movimiento savedMovimiento) {
		// Construir el DTO de respuesta
        MovimientoResponseDTO responseDTO = new MovimientoResponseDTO();
        responseDTO.setNumeroCuenta(cuenta.getNumeroCuenta());
        responseDTO.setTipoCuenta(cuenta.getTipoCuenta());
        responseDTO.setSaldoInicial(cuenta.getSaldoInicial());
        responseDTO.setEstado(cuenta.isEstado());
        responseDTO.setMontoMovimiento(savedMovimiento.getTipoMovimiento() + " de " + savedMovimiento.getValor());
		return responseDTO;
	}
}

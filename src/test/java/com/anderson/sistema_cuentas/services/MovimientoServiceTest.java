package com.anderson.sistema_cuentas.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;

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

public class MovimientoServiceTest {
	
    @Mock
    private MovimientoRepository movimientoRepository;

    @Mock
    private CuentaRepository cuentaRepository;

    @InjectMocks
    private MovimientoService movimientoService;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    public void testFindAll() {
        List<Movimiento> movimientos = new ArrayList<>();
        movimientos.add(new Movimiento());
        when(movimientoRepository.findAll()).thenReturn(movimientos);

        List<Movimiento> result = movimientoService.findAll();

        assertEquals(movimientos, result);
        verify(movimientoRepository, times(1)).findAll();
    }

    @Test
    public void testFindById_Success() {
        Long movimientoId = 1L;
        Movimiento movimiento = new Movimiento();
        when(movimientoRepository.findById(movimientoId)).thenReturn(Optional.of(movimiento));

        Optional<Movimiento> result = movimientoService.findById(movimientoId);

        assertTrue(result.isPresent());
        assertEquals(movimiento, result.get());
        verify(movimientoRepository, times(1)).findById(movimientoId);
    }

    @Test
    public void testFindById_NotFound() {
        Long movimientoId = 1L;
        when(movimientoRepository.findById(movimientoId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(MovimientoNotFoundException.class, () -> {
            movimientoService.findById(movimientoId);
        });

        String expectedMessage = "Movimiento con ID " + movimientoId + " no encontrado";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
        verify(movimientoRepository, times(1)).findById(movimientoId);
    }

    @Test
    public void testSave_CuentaNotFound() {
        Movimiento movimiento = new Movimiento();
        movimiento.setCuenta(new Cuenta());
        movimiento.getCuenta().setNumeroCuenta(123L);

        when(cuentaRepository.findByNumeroCuenta(123L)).thenReturn(null);

        Exception exception = assertThrows(CuentaNotFoundException.class, () -> {
            movimientoService.save(movimiento);
        });

        String expectedMessage = "La cuenta con número 123 no existe.";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
        verify(cuentaRepository, times(1)).findByNumeroCuenta(123L);
        verify(movimientoRepository, never()).save(any(Movimiento.class));
    }

    @Test
    public void testSave_InsufficientFunds() {
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(123L);
        cuenta.setSaldoInicial(BigDecimal.valueOf(1000));

        Movimiento movimiento = new Movimiento();
        movimiento.setCuenta(cuenta);
        movimiento.setValor(BigDecimal.valueOf(1500));
        movimiento.setTipoMovimiento(TipoMovimiento.RETIRO);

        when(cuentaRepository.findByNumeroCuenta(123L)).thenReturn(cuenta);
        when(movimientoRepository.findLatestMovimientoByCuentaId(cuenta.getId(), PageRequest.of(0, 1)))
            .thenReturn(new ArrayList<>());

        Exception exception = assertThrows(InsufficientFundsException.class, () -> {
            movimientoService.save(movimiento);
        });

        String expectedMessage = "Saldo no disponible para realizar el débito.";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
        verify(cuentaRepository, times(1)).findByNumeroCuenta(123L);
        verify(movimientoRepository, never()).save(any(Movimiento.class));
    }

    @Test
    public void testSave_DailyLimitExceeded() {
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(123L);
        cuenta.setLimiteDiarioRetiro(BigDecimal.valueOf(1000));
        cuenta.setSaldoInicial(BigDecimal.valueOf(1500)); // Asegúrate de que la cuenta tenga un saldo inicial adecuado

        Movimiento movimiento = new Movimiento();
        movimiento.setCuenta(cuenta);
        movimiento.setValor(BigDecimal.valueOf(1200)); // Valor que excede el límite diario
        movimiento.setTipoMovimiento(TipoMovimiento.RETIRO);

        // Configurar mocks
        when(cuentaRepository.findByNumeroCuenta(123L)).thenReturn(cuenta);
        when(movimientoRepository.sumValoresByNumeroCuentaAndFecha(cuenta.getId(), LocalDate.now()))
                .thenReturn(BigDecimal.valueOf(600)); // Total retirado hoy que, junto con el valor del movimiento, excede el límite diario

        // Verificar que se lanza la excepción correcta
        Exception exception = assertThrows(DailyLimitExceededException.class, () -> {
            movimientoService.save(movimiento);
        });

        String expectedMessage = "Cupo diario excedido. Solo se permite retirar hasta 400"; // Asegúrate de que el mensaje de excepción sea correcto
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
        verify(cuentaRepository, times(1)).findByNumeroCuenta(123L);
        verify(movimientoRepository, never()).save(any(Movimiento.class));
    }

    @Test
    public void testSave_Success() {
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(123L);
        cuenta.setSaldoInicial(BigDecimal.valueOf(1000));

        Movimiento movimiento = new Movimiento();
        movimiento.setCuenta(cuenta);
        movimiento.setValor(BigDecimal.valueOf(200));
        movimiento.setTipoMovimiento(TipoMovimiento.DEPOSITO);

        when(cuentaRepository.findByNumeroCuenta(123L)).thenReturn(cuenta);
        when(movimientoRepository.findLatestMovimientoByCuentaId(cuenta.getId(), PageRequest.of(0, 1)))
            .thenReturn(new ArrayList<>());
        when(movimientoRepository.save(any(Movimiento.class))).thenReturn(movimiento);

        MovimientoResponseDTO response = movimientoService.save(movimiento);

        assertNotNull(response);
        assertEquals(cuenta.getNumeroCuenta(), response.getNumeroCuenta());
        assertEquals("DEPOSITO de 200", response.getMontoMovimiento());
        verify(cuentaRepository, times(1)).findByNumeroCuenta(123L);
        verify(movimientoRepository, times(1)).save(movimiento);
    }

}

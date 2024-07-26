package com.anderson.sistema_cuentas.services;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.anderson.sistema_cuentas.entities.Cliente;
import com.anderson.sistema_cuentas.entities.Cuenta;
import com.anderson.sistema_cuentas.exception.CuentaNotFoundException;
import com.anderson.sistema_cuentas.exception.InsertException;
import com.anderson.sistema_cuentas.repositories.CuentaRepository;

public class CuentaServiceTest {
	 @Mock
	    private CuentaRepository cuentaRepository;

	    @InjectMocks
	    private CuentaService cuentaService;

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
	        List<Cuenta> cuentas = new ArrayList<>();
	        cuentas.add(new Cuenta());
	        when(cuentaRepository.findAll()).thenReturn(cuentas);

	        List<Cuenta> result = cuentaService.findAll();

	        assertEquals(cuentas, result);
	        verify(cuentaRepository, times(1)).findAll();
	    }

	    @Test
	    public void testFindById_Success() {
	        Long cuentaId = 1L;
	        Cuenta cuenta = new Cuenta();
	        when(cuentaRepository.findById(cuentaId)).thenReturn(Optional.of(cuenta));

	        Optional<Cuenta> result = cuentaService.findById(cuentaId);

	        assertTrue(result.isPresent());
	        assertEquals(cuenta, result.get());
	        verify(cuentaRepository, times(1)).findById(cuentaId);
	    }

	    @Test
	    public void testFindById_NotFound() {
	        Long cuentaId = 1L;
	        when(cuentaRepository.findById(cuentaId)).thenReturn(Optional.empty());

	        Exception exception = assertThrows(CuentaNotFoundException.class, () -> {
	            cuentaService.findById(cuentaId);
	        });

	        String expectedMessage = "Cuenta con número " + cuentaId + " no encontrada";
	        String actualMessage = exception.getMessage();

	        assertEquals(expectedMessage, actualMessage);
	        verify(cuentaRepository, times(1)).findById(cuentaId);
	    }

	    @Test
	    public void testSave_NewCuenta_Success() {
	        Cuenta cuenta = new Cuenta();
	        cuenta.setNumeroCuenta(123L);
	        cuenta.setCliente(new Cliente());

	        when(cuentaRepository.existsByNumeroCuentaAndCliente(cuenta.getNumeroCuenta(), cuenta.getCliente())).thenReturn(false);
	        when(cuentaRepository.save(cuenta)).thenReturn(cuenta);

	        Cuenta result = cuentaService.save(cuenta, true);

	        assertEquals(cuenta, result);
	        verify(cuentaRepository, times(1)).existsByNumeroCuentaAndCliente(cuenta.getNumeroCuenta(), cuenta.getCliente());
	        verify(cuentaRepository, times(1)).save(cuenta);
	    }

	    @Test
	    public void testSave_DuplicateCuenta_Exception() {
	        Cuenta cuenta = new Cuenta();
	        cuenta.setNumeroCuenta(123L);
	        cuenta.setCliente(new Cliente());

	        when(cuentaRepository.existsByNumeroCuentaAndCliente(cuenta.getNumeroCuenta(), cuenta.getCliente())).thenReturn(true);

	        assertThrows(InsertException.class, () -> {
	            cuentaService.save(cuenta, true);
	        });

	        verify(cuentaRepository, times(1)).existsByNumeroCuentaAndCliente(cuenta.getNumeroCuenta(), cuenta.getCliente());
	        verify(cuentaRepository, never()).save(cuenta);
	    }

	    @Test
	    public void testDeleteById_Success() {
	        Long cuentaId = 1L;
	        when(cuentaRepository.existsById(cuentaId)).thenReturn(true);

	        cuentaService.deleteById(cuentaId);

	        verify(cuentaRepository, times(1)).existsById(cuentaId);
	        verify(cuentaRepository, times(1)).deleteById(cuentaId);
	    }

	    @Test
	    public void testDeleteById_NotFound() {
	        Long cuentaId = 1L;
	        when(cuentaRepository.existsById(cuentaId)).thenReturn(false);

	        Exception exception = assertThrows(CuentaNotFoundException.class, () -> {
	            cuentaService.deleteById(cuentaId);
	        });

	        String expectedMessage = "Cuenta con número " + cuentaId + " no encontrada";
	        String actualMessage = exception.getMessage();

	        assertEquals(expectedMessage, actualMessage);
	        verify(cuentaRepository, times(1)).existsById(cuentaId);
	        verify(cuentaRepository, never()).deleteById(cuentaId);
	    }
}

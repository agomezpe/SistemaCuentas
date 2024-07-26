package com.anderson.sistema_cuentas.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.lang.AutoCloseable;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.anderson.sistema_cuentas.entities.Cliente;
import com.anderson.sistema_cuentas.entities.Persona;
import com.anderson.sistema_cuentas.exception.ClientHasAccountsException;
import com.anderson.sistema_cuentas.exception.ClienteNotFoundException;
import com.anderson.sistema_cuentas.exception.InsertException;
import com.anderson.sistema_cuentas.repositories.ClienteRepository;
import com.anderson.sistema_cuentas.repositories.CuentaRepository;
import com.anderson.sistema_cuentas.repositories.PersonaRepository;

public class ClienteServiceTest {

    @InjectMocks
    private ClienteService clienteService;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private PersonaRepository personaRepository;

    @Mock
    private CuentaRepository cuentaRepository;
    
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
    public void testFindAll_Success() {
        Cliente cliente = new Cliente();
        when(clienteRepository.findAll()).thenReturn(Arrays.asList(cliente));

        List<Cliente> result = clienteService.findAll();

        assertEquals(1, result.size());
        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    public void testFindAll_NoClients() {
        when(clienteRepository.findAll()).thenReturn(Arrays.asList());

        ClienteNotFoundException exception = assertThrows(ClienteNotFoundException.class, () -> {
            clienteService.findAll();
        });

        assertEquals("No se encontraron clientes.", exception.getMessage());
    }

    @Test
    public void testFindById_Success() {
        // Arrange
        Long clienteId = 1L;
        Cliente cliente = new Cliente(); // Aquí puedes configurar el cliente según sea necesario
        when(clienteRepository.existsById(clienteId)).thenReturn(true);
        when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(cliente));

        // Act
        Optional<Cliente> result = clienteService.findById(clienteId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(cliente, result.get());
        verify(clienteRepository, times(1)).existsById(clienteId);
        verify(clienteRepository, times(2)).findById(clienteId);
    }

    @Test
    public void testFindById_NotFound() {
        when(clienteRepository.existsById(1L)).thenReturn(false);

        ClienteNotFoundException exception = assertThrows(ClienteNotFoundException.class, () -> {
            clienteService.findById(1L);
        });

        assertEquals("Cliente con ID 1 no encontrado", exception.getMessage());
    }

    @Test
    public void testSave_NewCliente_Success() {
        Cliente cliente = new Cliente();
        Persona persona = new Persona();
        persona.setIdentificacion("123456");
        cliente.setPersona(persona);

        when(personaRepository.existsByIdentificacion("123456")).thenReturn(false);
        when(personaRepository.save(persona)).thenReturn(persona);
        when(clienteRepository.save(cliente)).thenReturn(cliente);

        Cliente result = clienteService.save(cliente, true);

        assertEquals(cliente, result);
        verify(personaRepository, times(1)).save(persona);
        verify(clienteRepository, times(1)).save(cliente);
    }

    @Test
    public void testSave_ExistingCliente_IdentificacionInUse() {
        Cliente cliente = new Cliente();
        Persona persona = new Persona();
        persona.setIdentificacion("123456");
        cliente.setPersona(persona);

        when(personaRepository.existsByIdentificacion("123456")).thenReturn(true);

        InsertException exception = assertThrows(InsertException.class, () -> {
            clienteService.save(cliente, true);
        });

        assertEquals("La identificación ya está en uso: 123456", exception.getMessage());
    }

    @Test
    public void testDeleteById_Success() {
        Cliente cliente = new Cliente();
        Persona persona = new Persona();
        persona.setId(1L);
        cliente.setPersona(persona);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(cuentaRepository.existsByCliente_ClienteId(1L)).thenReturn(false);

        clienteService.deleteById(1L);

        verify(clienteRepository, times(1)).deleteById(1L);
        verify(personaRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteById_ClientHasAccounts() {
        Cliente cliente = new Cliente();
        Persona persona = new Persona();
        persona.setId(1L);
        cliente.setPersona(persona);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(cuentaRepository.existsByCliente_ClienteId(1L)).thenReturn(true);

        ClientHasAccountsException exception = assertThrows(ClientHasAccountsException.class, () -> {
            clienteService.deleteById(1L);
        });

        assertEquals("Cliente con ID 1 tiene cuentas asociadas.", exception.getMessage());
    }
}
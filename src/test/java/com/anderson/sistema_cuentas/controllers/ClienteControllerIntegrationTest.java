package com.anderson.sistema_cuentas.controllers;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.anderson.sistema_cuentas.config.TestConfig;
import com.anderson.sistema_cuentas.dto.ClienteDTO;
import com.anderson.sistema_cuentas.entities.Cliente;
import com.anderson.sistema_cuentas.entities.Persona;
import com.anderson.sistema_cuentas.exception.InsertException;
import com.anderson.sistema_cuentas.services.ClienteService;
import com.anderson.sistema_cuentas.services.PersonaService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ClienteController.class)
@Import(TestConfig.class) // Importa la configuración de prueba
public class ClienteControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService clienteService;

    @MockBean
    private PersonaService personaService;

    @Autowired
    private ObjectMapper objectMapper;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllClientes() throws Exception {
        when(clienteService.findAll()).thenReturn(List.of(new Cliente()));
        
        mockMvc.perform(MockMvcRequestBuilders.get("/clientes"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testCreateCliente_Success() throws Exception {
        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setNombre("Juan");
        clienteDTO.setGenero("Masculino");
        clienteDTO.setEdad(30);
        clienteDTO.setIdentificacion("123456789");
        clienteDTO.setDireccion("Direccion");
        clienteDTO.setTelefono("987654321");
        clienteDTO.setContrasena("123456");
        clienteDTO.setEstado(true);

        // Crear objeto Persona
        Persona persona = new Persona();
        persona.setNombre("Juan");
        persona.setGenero("Masculino");
        persona.setEdad(30);
        persona.setIdentificacion("123456789");
        persona.setDireccion("Direccion");
        persona.setTelefono("987654321");
        persona.setId(1L);

        // Crear objeto Cliente
        Cliente cliente = new Cliente();
        cliente.setContrasena("123456");
        cliente.setEstado(true);
        cliente.setPersona(persona);
        cliente.setClienteId(1L); // Simular un ID generado

        // Configurar el mock para devolver el cliente simulado
        when(clienteService.save(any(Cliente.class), eq(true))).thenReturn(cliente);

        // Realizar la solicitud POST
        mockMvc.perform(MockMvcRequestBuilders.post("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.clienteId", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nombre", is("Juan")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genero", is("Masculino")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.edad", is(30)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.identificacion", is("123456789")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.direccion", is("Direccion")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.telefono", is("987654321")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.contrasena", is("123456")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.estado", is(true)))
                ;
    }

    @Test
    void testCreateCliente_ValidationErrors() throws Exception {
        ClienteDTO clienteDTO = new ClienteDTO();
        // Intencionalmente dejamos campos necesarios vacíos para generar errores de validación

        mockMvc.perform(MockMvcRequestBuilders.post("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nombre", is("El nombre no puede estar vacío")));
    }

    @Test
    void testCreateCliente_IdentificationAlreadyInUse() throws Exception {
        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setNombre("Juan");
        clienteDTO.setGenero("Masculino");
        clienteDTO.setEdad(30);
        clienteDTO.setIdentificacion("123456789");
        clienteDTO.setDireccion("Direccion");
        clienteDTO.setTelefono("987654321");
        clienteDTO.setContrasena("123456");
        clienteDTO.setEstado(true);

        when(clienteService.save(any(Cliente.class), eq(true)))
            .thenThrow(new InsertException("La identificación ya está en uso: " + clienteDTO.getIdentificacion()));

        mockMvc.perform(MockMvcRequestBuilders.post("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteDTO)))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.content().string("La identificación ya está en uso: " + clienteDTO.getIdentificacion()));
    }
}
package com.anderson.sistema_cuentas.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.anderson.sistema_cuentas.dto.ClienteDTO;
import com.anderson.sistema_cuentas.dto.CuentaDTO;
import com.anderson.sistema_cuentas.entities.Cliente;
import com.anderson.sistema_cuentas.entities.Cuenta;

@Configuration
public class TestConfig {
	
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.typeMap(Cliente.class, ClienteDTO.class).addMappings(mapper -> {
            mapper.map(src -> src.getPersona().getNombre(), ClienteDTO::setNombre);
            mapper.map(src -> src.getPersona().getGenero(), ClienteDTO::setGenero);
            mapper.map(src -> src.getPersona().getEdad(), ClienteDTO::setEdad);
            mapper.map(src -> src.getPersona().getIdentificacion(), ClienteDTO::setIdentificacion);
            mapper.map(src -> src.getPersona().getDireccion(), ClienteDTO::setDireccion);
            mapper.map(src -> src.getPersona().getTelefono(), ClienteDTO::setTelefono);
        });
        modelMapper.addMappings(new PropertyMap<Cuenta, CuentaDTO>() {
            @Override
            protected void configure() {
                map().setNombreCliente(source.getCliente().getPersona().getNombre());
            }
        });
        return modelMapper;
    }

}

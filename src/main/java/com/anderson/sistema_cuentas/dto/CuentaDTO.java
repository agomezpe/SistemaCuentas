package com.anderson.sistema_cuentas.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CuentaDTO {
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Long id;
	
	@NotNull(message = "El número de cuenta no puede ser nulo")
    private Long numeroCuenta;
	
	@Pattern(regexp = "Ahorros|Corriente", message = "El tipo de cuenta debe ser 'Ahorros' o 'Corriente'")
    @NotBlank(message = "El tipo de cuenta no puede estar vacío")
    private String tipoCuenta;

    @Positive(message = "El saldo inicial debe ser positivo")
    private BigDecimal saldoInicial;

    @NotNull(message = "El estado no puede ser nulo")
    private boolean estado;
    
    @NotNull(message = "El ID del cliente no puede ser nulo")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long clienteId; // Para vincular la cuenta con un cliente específico
    
    private String nombreCliente;
    

	/**
	 * @return the numeroCuenta
	 */
	public Long getNumeroCuenta() {
		return numeroCuenta;
	}
	/**
	 * @param numeroCuenta the numeroCuenta to set
	 */
	public void setNumeroCuenta(Long numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}
	/**
	 * @return the tipoCuenta
	 */
	public String getTipoCuenta() {
		return tipoCuenta;
	}
	/**
	 * @param tipoCuenta the tipoCuenta to set
	 */
	public void setTipoCuenta(String tipoCuenta) {
		this.tipoCuenta = tipoCuenta;
	}

	/**
	 * @return the estado
	 */
	public boolean isEstado() {
		return estado;
	}
	/**
	 * @param estado the estado to set
	 */
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	/**
	 * @return the clienteId
	 */
	public Long getClienteId() {
		return clienteId;
	}
	/**
	 * @param clienteId the clienteId to set
	 */
	public void setClienteId(Long clienteId) {
		this.clienteId = clienteId;
	}
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the saldoInicial
	 */
	public BigDecimal getSaldoInicial() {
		return saldoInicial;
	}
	/**
	 * @param saldoInicial the saldoInicial to set
	 */
	public void setSaldoInicial(BigDecimal saldoInicial) {
		this.saldoInicial = saldoInicial;
	}
	/**
	 * @return the nombreCliente
	 */
	public String getNombreCliente() {
		return nombreCliente;
	}
	/**
	 * @param nombreCliente the nombreCliente to set
	 */
	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}
    
    
}

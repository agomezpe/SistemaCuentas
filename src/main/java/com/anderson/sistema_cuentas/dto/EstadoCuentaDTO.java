package com.anderson.sistema_cuentas.dto;

import java.math.BigDecimal;
import java.util.List;

public class EstadoCuentaDTO {
    private Long clienteId;
    private List<CuentaReporteDTO> cuentas;
    private BigDecimal totalDebitos;
    private BigDecimal totalCreditos;
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
	 * @return the cuentas
	 */
	public List<CuentaReporteDTO> getCuentas() {
		return cuentas;
	}
	/**
	 * @param cuentas the cuentas to set
	 */
	public void setCuentas(List<CuentaReporteDTO> cuentas) {
		this.cuentas = cuentas;
	}
	/**
	 * @return the totalDebitos
	 */
	public BigDecimal getTotalDebitos() {
		return totalDebitos;
	}
	/**
	 * @param totalDebitos the totalDebitos to set
	 */
	public void setTotalDebitos(BigDecimal totalDebitos) {
		this.totalDebitos = totalDebitos;
	}
	/**
	 * @return the totalCreditos
	 */
	public BigDecimal getTotalCreditos() {
		return totalCreditos;
	}
	/**
	 * @param totalCreditos the totalCreditos to set
	 */
	public void setTotalCreditos(BigDecimal totalCreditos) {
		this.totalCreditos = totalCreditos;
	}
    
    
}

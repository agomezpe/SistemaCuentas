package com.anderson.sistema_cuentas.dto;

import java.math.BigDecimal;

public class MovimientoResponseDTO {
    private Long numeroCuenta;
    private String tipoCuenta;
    private BigDecimal saldoInicial;
    private boolean estado;
    private String montoMovimiento;
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
	 * @return the montoMovimiento
	 */
	public String getMontoMovimiento() {
		return montoMovimiento;
	}
	/**
	 * @param montoMovimiento the montoMovimiento to set
	 */
	public void setMontoMovimiento(String montoMovimiento) {
		this.montoMovimiento = montoMovimiento;
	}

    
    
}

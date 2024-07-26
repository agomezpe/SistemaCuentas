package com.anderson.sistema_cuentas.dto;

import java.math.BigDecimal;
import java.util.List;

public class CuentaReporteDTO {
    private Long numeroCuenta;
    private String tipoCuenta;
    private BigDecimal saldo;
    private BigDecimal totalDebitos;
    private BigDecimal totalCreditos;
    private BigDecimal saldoInicial;
    private boolean estadoCuenta;
    private List<MovimientoDTO> movimientos;
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
	 * @return the saldo
	 */
	public BigDecimal getSaldo() {
		return saldo;
	}
	/**
	 * @param saldo the saldo to set
	 */
	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
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
	 * @return the estadoCuenta
	 */
	public boolean isEstadoCuenta() {
		return estadoCuenta;
	}
	/**
	 * @param estadoCuenta the estadoCuenta to set
	 */
	public void setEstadoCuenta(boolean estadoCuenta) {
		this.estadoCuenta = estadoCuenta;
	}
	/**
	 * @return the movimientos
	 */
	public List<MovimientoDTO> getMovimientos() {
		return movimientos;
	}
	/**
	 * @param movimientos the movimientos to set
	 */
	public void setMovimientos(List<MovimientoDTO> movimientos) {
		this.movimientos = movimientos;
	}

    
    
}

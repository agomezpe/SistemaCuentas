package com.anderson.sistema_cuentas.entities;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "cuenta", uniqueConstraints = {
	    @UniqueConstraint(columnNames = {"numeroCuenta", "clienteId"})
	})
public class Cuenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long numeroCuenta;
	private String tipoCuenta;
    private BigDecimal saldoInicial;
    private boolean estado;
    private BigDecimal limiteDiarioRetiro = BigDecimal.valueOf(1000); // Valor por defecto de $1000

    @ManyToOne
    @JoinColumn(name = "clienteId")
    private Cliente cliente;

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
	 * @return the cliente
	 */
	public Cliente getCliente() {
		return cliente;
	}

	/**
	 * @param cliente the cliente to set
	 */
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
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
	 * @return the limiteDiarioRetiro
	 */
	public BigDecimal getLimiteDiarioRetiro() {
		return limiteDiarioRetiro;
	}

	/**
	 * @param limiteDiarioRetiro the limiteDiarioRetiro to set
	 */
	public void setLimiteDiarioRetiro(BigDecimal limiteDiarioRetiro) {
		this.limiteDiarioRetiro = limiteDiarioRetiro;
	}
    
    
}

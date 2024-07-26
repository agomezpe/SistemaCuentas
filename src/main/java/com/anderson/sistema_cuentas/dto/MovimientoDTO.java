package com.anderson.sistema_cuentas.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.anderson.sistema_cuentas.enums.TipoMovimiento;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class MovimientoDTO {
	@JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;
    
    @NotNull(message = "La fecha no puede ser nula")
    private LocalDate fecha;
    
    @NotNull(message = "El tipoMovimiento no puede ser nula")
    private TipoMovimiento tipoMovimiento;
    
    @NotNull(message = "El valor no puede ser nulo")
    @Positive(message = "El valor debe ser positivo")
    private BigDecimal valor;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal saldo;
    
    @NotNull(message = "El número de cuenta no puede ser nulo")
    @Min(value = 1, message = "El número de cuenta debe ser mayor que 0")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long numeroCuenta; // Para vincular el movimiento con una cuenta específica

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
	 * @return the fecha
	 */
	public LocalDate getFecha() {
		return fecha;
	}

	/**
	 * @param fecha the fecha to set
	 */
	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}



	/**
	 * @return the valor
	 */
	public BigDecimal getValor() {
		return valor;
	}

	/**
	 * @param valor the valor to set
	 */
	public void setValor(BigDecimal valor) {
		this.valor = valor;
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
	 * @return the tipoMovimiento
	 */
	public TipoMovimiento getTipoMovimiento() {
		return tipoMovimiento;
	}

	/**
	 * @param tipoMovimiento the tipoMovimiento to set
	 */
	public void setTipoMovimiento(TipoMovimiento tipoMovimiento) {
		this.tipoMovimiento = tipoMovimiento;
	}
    
    
}

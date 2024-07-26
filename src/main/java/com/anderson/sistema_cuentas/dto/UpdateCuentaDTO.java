package com.anderson.sistema_cuentas.dto;

import jakarta.validation.constraints.Pattern;

public class UpdateCuentaDTO {
	@Pattern(regexp = "Ahorros|Corriente", message = "El tipo de cuenta debe ser 'Ahorros' o 'Corriente'")
    private String tipoCuenta;

    private boolean estado;

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
   
    
}

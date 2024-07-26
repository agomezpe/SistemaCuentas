package com.anderson.sistema_cuentas.dto;

public class ClienteCuentaResponseDTO extends CuentaDTO {
	
	private String nombreCliente;

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

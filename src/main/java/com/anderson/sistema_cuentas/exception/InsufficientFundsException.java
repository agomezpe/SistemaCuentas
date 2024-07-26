package com.anderson.sistema_cuentas.exception;

public class InsufficientFundsException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6889744270494057887L;
	
	public InsufficientFundsException(String message) {
		super(message);
	}

}

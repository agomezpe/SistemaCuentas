package com.anderson.sistema_cuentas.exception;

public class DailyLimitExceededException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1767773097547979573L;
	
	public DailyLimitExceededException(String message) {
		super(message);
	}

}

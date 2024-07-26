package com.anderson.sistema_cuentas.exception;

public class MovimientoNotFoundException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = -4482643242065334235L;

	public MovimientoNotFoundException(String message) {
        super(message);
    }
}

package com.anderson.sistema_cuentas.exception;

public class CuentaNotFoundException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 2644494257800590334L;

	public CuentaNotFoundException(String message) {
        super(message);
    }
}

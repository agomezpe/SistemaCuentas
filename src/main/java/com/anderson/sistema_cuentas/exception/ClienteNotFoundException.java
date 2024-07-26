package com.anderson.sistema_cuentas.exception;

public class ClienteNotFoundException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = -8799133193812553547L;

	public ClienteNotFoundException(String message) {
        super(message);
    }
}

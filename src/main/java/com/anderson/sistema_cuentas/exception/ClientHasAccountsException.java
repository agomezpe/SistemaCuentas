package com.anderson.sistema_cuentas.exception;

public class ClientHasAccountsException extends RuntimeException{
    /**
	 * 
	 */
	private static final long serialVersionUID = 278521762377247356L;

	public ClientHasAccountsException(String message) {
        super(message);
    }
}
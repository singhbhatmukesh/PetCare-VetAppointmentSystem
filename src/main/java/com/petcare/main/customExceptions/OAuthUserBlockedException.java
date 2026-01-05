package com.petcare.main.customExceptions;

public class OAuthUserBlockedException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OAuthUserBlockedException(String message) {
        super(message);
    }
}
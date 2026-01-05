package com.petcare.main.customExceptions;

public class TokenExpiredException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TokenExpiredException(String msg) {
		super(msg);
	}

}

package com.challenge.fastfood.service_io.exceptions;

public class InvalidJwtStateException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public InvalidJwtStateException(String msg) {
		super(msg);
	}

	public InvalidJwtStateException(String msg, Throwable cause) {
		super(msg, cause);
	}

}

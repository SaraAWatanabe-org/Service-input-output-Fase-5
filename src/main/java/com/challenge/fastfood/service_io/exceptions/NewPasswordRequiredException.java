package com.challenge.fastfood.service_io.exceptions;

public class NewPasswordRequiredException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	private String session;

	public NewPasswordRequiredException(String msg, String session) {
		super(msg);
		this.session = session;
	}

	public NewPasswordRequiredException(String msg, Throwable cause, String session) {
		super(msg, cause);
		this.session = session;
	}

	public String getSession() {
		return session;
	}

}

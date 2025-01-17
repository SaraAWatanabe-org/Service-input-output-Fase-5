package com.challenge.fastfood.service_io.exceptions;

public class FileIOException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public FileIOException(String msg) {
		super(msg);
	}

	public FileIOException(String msg, Throwable cause) {
		super(msg, cause);
	}

}

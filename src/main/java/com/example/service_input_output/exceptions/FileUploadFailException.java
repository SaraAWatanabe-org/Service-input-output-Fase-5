package com.example.service_input_output.exceptions;

public class FileUploadFailException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public FileUploadFailException(String msg) {
		super(msg);
	}

	public FileUploadFailException(String msg, Throwable cause) {
		super(msg, cause);
	}

}

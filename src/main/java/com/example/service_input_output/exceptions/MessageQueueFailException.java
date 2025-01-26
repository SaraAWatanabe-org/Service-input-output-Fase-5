package com.example.service_input_output.exceptions;

public class MessageQueueFailException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public MessageQueueFailException(String msg) {
		super(msg);
	}

	public MessageQueueFailException(String msg, Throwable cause) {
		super(msg, cause);
	}

}

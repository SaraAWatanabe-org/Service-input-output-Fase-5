package com.challenge.fastfood.service_io.exceptions;

public class MessageQueueFailException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public MessageQueueFailException(String msg) {
		super(msg);
	}

	public MessageQueueFailException(String msg, Throwable cause) {
		super(msg, cause);
	}

}

package com.challenge.fastfood.service_io.exceptions.handler;

public enum LoginErrorCodeEnum {

	INVALID_CREDENTIALS("INVALID_CREDENTIALS"),
	NEW_PASSWORD_REQUIRED("NEW_PASSWORD_REQUIRED"),
	LOGIN_FAIL("LOGIN_FAIL"),
	;

	private String code;

	private LoginErrorCodeEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

}

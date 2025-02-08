package com.example.service_input_output.exceptions.handler;

public enum ExceptionErrorCodeEnum {

	STANDARD_ERROR("STANDARD_ERROR"),
	//ERROR_LIST("ERROR_LIST"),
	FORM_FIELD_ERROR_LIST("FORM_FIELD_ERROR_LIST"),
	LOGIN_ERROR("LOGIN_ERROR"),
	;

	private String code;


	private ExceptionErrorCodeEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

}

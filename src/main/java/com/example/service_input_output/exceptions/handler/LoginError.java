package com.example.service_input_output.exceptions.handler;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginError implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer status;
	private String msg;
	private Long timeStamp;
	private String loginErrorCode;
	private String session;
	private final String code = ExceptionErrorCodeEnum.LOGIN_ERROR.getCode();

	public LoginError(Integer status, String msg, Long timeStamp, LoginErrorCodeEnum loginErrorCode) {
		super();
		this.status = status;
		this.msg = msg;
		this.timeStamp = timeStamp;
		this.loginErrorCode = loginErrorCode.getCode();
	}

	public LoginError(Integer status, String msg, Long timeStamp, LoginErrorCodeEnum loginErrorCode, String session) {
		super();
		this.status = status;
		this.msg = msg;
		this.timeStamp = timeStamp;
		this.loginErrorCode = loginErrorCode.getCode();
		this.session = session;
	}

}

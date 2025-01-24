package com.challenge.fastfood.service_io.exceptions.handler;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FormsError {

	private Integer status;
	private String msg;
	private Long timeStamp;
	private List<FormFieldError> errors;

	private String code = ExceptionErrorCodeEnum.FORM_FIELD_ERROR_LIST.getCode();

	public FormsError(Integer status, String msg, Long timeStamp, List<FormFieldError> errors) {
		super();
		this.status = status;
		this.msg = msg;
		this.timeStamp = timeStamp;
		this.errors = errors;
	}

}

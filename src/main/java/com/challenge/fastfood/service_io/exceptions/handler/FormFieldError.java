package com.challenge.fastfood.service_io.exceptions.handler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FormFieldError {

	private String field;
	private String error;

}

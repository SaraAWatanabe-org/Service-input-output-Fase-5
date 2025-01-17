package com.challenge.fastfood.service_io.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorMessageDto {

	private Integer id;
	private String code;
	private String description;

}

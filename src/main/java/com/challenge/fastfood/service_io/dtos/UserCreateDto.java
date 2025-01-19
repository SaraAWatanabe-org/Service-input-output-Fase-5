package com.challenge.fastfood.service_io.dtos;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDto {

	@NotBlank
	private String username;
	
	@NotBlank
	private String name;

	@Email
	private String email;

	@NotBlank
	private String password;

	@NotBlank
	@Length(min = 11, max = 11)
	private String cpf;

}

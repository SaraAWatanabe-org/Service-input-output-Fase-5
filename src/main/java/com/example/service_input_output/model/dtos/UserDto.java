package com.example.service_input_output.model.dtos;

import org.springframework.beans.BeanUtils;

import com.example.service_input_output.entities.UserEntity;

import lombok.Data;

@Data
public class UserDto {

	private String name;
	private String email;
	private String cpf;
	private Boolean isTermAccepted;

	public  UserDto (UserEntity userEntity) {
		BeanUtils.copyProperties(userEntity, this);
	}

}

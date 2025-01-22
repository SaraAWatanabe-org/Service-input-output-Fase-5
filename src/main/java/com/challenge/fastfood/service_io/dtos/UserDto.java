package com.challenge.fastfood.service_io.dtos;

import org.springframework.beans.BeanUtils;

import com.challenge.fastfood.service_io.entities.UserEntity;

import lombok.Data;

@Data
public class UserDto {

	private String username;
	private String name;
	private String email;
	private String cpf;
	private Boolean isTermAccepted;

	public  UserDto (UserEntity userEntity) {
		BeanUtils.copyProperties(userEntity, this);
	}

}

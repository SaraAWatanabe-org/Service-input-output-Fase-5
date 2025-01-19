package com.challenge.fastfood.service_io.services;

import com.challenge.fastfood.service_io.dtos.UserCreateDto;
import com.challenge.fastfood.service_io.dtos.UserDto;

public interface CognitoService {

	String login(String username, String password);

	String respondToNewPasswordRequired(String session, String username, String newPassword);

	UserDto createUser(UserCreateDto userCreateDto);

}

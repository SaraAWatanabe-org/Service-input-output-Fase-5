package com.example.service_input_output.service;

import com.example.service_input_output.model.dtos.UserCreateDto;
import com.example.service_input_output.model.dtos.UserDto;

public interface CognitoService {

	String login(String username, String password);

	String respondToNewPasswordRequired(String session, String username, String newPassword);

	UserDto createUser(UserCreateDto userCreateDto);

	void acceptTermsOfUse();

}

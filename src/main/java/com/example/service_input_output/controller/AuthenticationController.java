package com.example.service_input_output.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.service_input_output.model.dtos.AuthenticationResponseDto;
import com.example.service_input_output.model.dtos.LoginRequestDto;
import com.example.service_input_output.model.dtos.NewPasswordRequestDto;
import com.example.service_input_output.model.dtos.UserCreateDto;
import com.example.service_input_output.model.dtos.UserDto;
import com.example.service_input_output.service.CognitoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Authentication", description = "Authentication Controller")
public class AuthenticationController {

	@Autowired
	private CognitoService cognitoService;

	@PostMapping("/login")
	@Operation(summary = "Login", description = "Login")
	public ResponseEntity<AuthenticationResponseDto> login(@RequestBody LoginRequestDto loginRequest) {
		String token = cognitoService.login(loginRequest.getUsername(), loginRequest.getPassword());
		return ResponseEntity.ok(new AuthenticationResponseDto(token));
	}

	@PostMapping("/respond-to-new-password")
	@Operation(summary = "Respond to new Password", description = "Respond to new password requesto for the user")
	public ResponseEntity<AuthenticationResponseDto> respondToNewPassword(@RequestBody NewPasswordRequestDto request) {
		String token = cognitoService.respondToNewPasswordRequired(request.getSession(), request.getUsername(), request.getNewPassword());
		return ResponseEntity.ok(new AuthenticationResponseDto(token));
	}

	@PostMapping("/create-user")
	@Operation(summary = "Create User", description = "Create a user in Cognito Service")
	public ResponseEntity<UserDto> create(@RequestBody UserCreateDto userCreateDto) {
		UserDto userDto = cognitoService.createUser(userCreateDto);
		return ResponseEntity.ok(userDto);
	}

	@PostMapping("/accept-terms")
	@Operation(summary = "Accept Terms", description = "Accept terms of use")
	public ResponseEntity<Void> acceptTerms() {
		cognitoService.acceptTermsOfUse();
		return ResponseEntity.noContent().build();
	}

}

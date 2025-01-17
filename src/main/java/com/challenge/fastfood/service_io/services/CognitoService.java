package com.challenge.fastfood.service_io.services;

public interface CognitoService {

	String login(String username, String password);

	String respondToNewPasswordRequired(String session, String username, String newPassword);

}

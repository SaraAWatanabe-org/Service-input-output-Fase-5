package com.example.service_input_output.service.impl;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.AdminAddUserToGroupRequest;
import com.amazonaws.services.cognitoidp.model.AdminCreateUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminCreateUserResult;
import com.amazonaws.services.cognitoidp.model.AttributeType;
import com.amazonaws.services.cognitoidp.model.AuthFlowType;
import com.amazonaws.services.cognitoidp.model.ChallengeNameType;
import com.amazonaws.services.cognitoidp.model.InitiateAuthRequest;
import com.amazonaws.services.cognitoidp.model.InitiateAuthResult;
import com.amazonaws.services.cognitoidp.model.RespondToAuthChallengeRequest;
import com.amazonaws.services.cognitoidp.model.RespondToAuthChallengeResult;
import com.example.service_input_output.entities.UserEntity;
import com.example.service_input_output.enums.UserRoleEnum;
import com.example.service_input_output.exceptions.DataIntegrityException;
import com.example.service_input_output.exceptions.LoginFailException;
import com.example.service_input_output.exceptions.NewPasswordRequiredException;
import com.example.service_input_output.exceptions.ObjectNotFoundException;
import com.example.service_input_output.model.dtos.UserCreateDto;
import com.example.service_input_output.model.dtos.UserDto;
import com.example.service_input_output.repositories.UserRepository;
import com.example.service_input_output.service.CognitoService;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class CognitoServiceImpl implements CognitoService {

	@Value("${aws.cognito.userPoolId}")
	private String userPoolId;

	@Value("${aws.cognito.client.id}")
	private String clientId;

	@Value("${aws.cognito.client.secret}")
	private String clientSecret;

	@Autowired
	private AWSCognitoIdentityProvider cognitoClient;

	@Autowired
	private UserRepository userRepository;

	@Override
	public String login(String username, String password) {
		String secretHash = calculateSecretHash(clientId, clientSecret, username);

		Map<String, String> authParams = new HashMap<>();
		authParams.put("USERNAME", username);
		authParams.put("PASSWORD", password);
		authParams.put("SECRET_HASH", secretHash);

		InitiateAuthRequest authRequest = new InitiateAuthRequest()
				.withClientId(clientId)
				.withAuthFlow(AuthFlowType.USER_PASSWORD_AUTH)
				.withAuthParameters(authParams);

		try {
			InitiateAuthResult authResponse = cognitoClient.initiateAuth(authRequest);
			if (authResponse.getChallengeName() != null && authResponse.getChallengeName().equals(ChallengeNameType.NEW_PASSWORD_REQUIRED.toString())) {
				throw new NewPasswordRequiredException("Necessário alterar a senha", authResponse.getSession());
			}
			return authResponse.getAuthenticationResult().getIdToken();
		} catch (NewPasswordRequiredException e) {
			throw e;
		} catch (Exception e) {
			throw new LoginFailException("Falha para efeturar login");
		}

	}

	@Override
	public String respondToNewPasswordRequired(String session, String username, String newPassword) {
		String secretHash = calculateSecretHash(clientId, clientSecret, username);

		Map<String, String> challengeResponses = new HashMap<>();
		challengeResponses.put("USERNAME", username);
		challengeResponses.put("NEW_PASSWORD", newPassword);
		challengeResponses.put("SECRET_HASH", secretHash);

		RespondToAuthChallengeRequest request = new RespondToAuthChallengeRequest()
				.withClientId(clientId)
				.withChallengeName(ChallengeNameType.NEW_PASSWORD_REQUIRED)
				.withSession(session)
				.withChallengeResponses(challengeResponses);

		try {
			RespondToAuthChallengeResult response = cognitoClient.respondToAuthChallenge(request);
			return response.getAuthenticationResult().getIdToken();
		} catch (Exception e) {
			throw new LoginFailException(e.getMessage());
		}
	}

	@Override
	public UserDto createUser(UserCreateDto userCreateDto) {
		Optional<UserEntity> userOptional = this.userRepository.findById(userCreateDto.getEmail());
		if(userOptional.isPresent()) {
			throw new DataIntegrityException("Já existe um usuário com esse username");
		}

		List<AttributeType> attributes = new ArrayList<>();
		attributes.add(new AttributeType().withName("name").withValue(userCreateDto.getName()));
		attributes.add(new AttributeType().withName("email").withValue(userCreateDto.getEmail()));

		AdminCreateUserRequest createUserRequest = new AdminCreateUserRequest()
				.withUserPoolId(userPoolId)
				.withUsername(userCreateDto.getEmail())
				.withTemporaryPassword(userCreateDto.getPassword())
				.withUserAttributes(attributes);

		//TODO EXCEPTION
		AdminCreateUserResult response = cognitoClient.adminCreateUser(createUserRequest);

		UserEntity userEntity = new UserEntity();
		userEntity.setEmail(userCreateDto.getEmail());
		userEntity.setName(userCreateDto.getName());
		userEntity.setCpf(userCreateDto.getCpf());
		userEntity.setIsTermAccepted(false);
		userRepository.save(userEntity);

		return new UserDto(userEntity);
	}

	//TODO CUSTOM EXCEPTION
	private void addUserToGroup(String email, UserRoleEnum role) {
		AdminAddUserToGroupRequest addUserToGroupRequest = new AdminAddUserToGroupRequest()
				.withUserPoolId(userPoolId)
				.withUsername(email)
				.withGroupName(role.getCode());
		cognitoClient.adminAddUserToGroup(addUserToGroupRequest);
	}

	private static String calculateSecretHash(String userPoolClientId, String userPoolClientSecret, String username) {
		try {
			String message = username + userPoolClientId;
			Mac mac = Mac.getInstance("HmacSHA256");
			SecretKeySpec secretKeySpec = new SecretKeySpec(userPoolClientSecret.getBytes(), "HmacSHA256");
			mac.init(secretKeySpec);
			byte[] rawHmac = mac.doFinal(message.getBytes());
			return Base64.getEncoder().encodeToString(rawHmac);
		} catch (Exception e) {
			throw new RuntimeException("Error calculating secret hash", e);
		}
	}

	@Override
	@Transactional
	public void acceptTermsOfUse() {
		UserEntity userEntity = this.getLoggedUser();
		if(userEntity.getIsTermAccepted()) {
			throw new DataIntegrityException("User has already accepted the terms of use.");
		}

		this.addUserToGroup(userEntity.getEmail(), UserRoleEnum.USER);

		userEntity.setIsTermAccepted(true);
		this.userRepository.save(userEntity);
	}

	private UserEntity getLoggedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Jwt jwt = (Jwt) authentication.getPrincipal();
		String email = jwt.getClaim("email");

		return findUserByUsername(email);
	}

	private UserEntity findUserByUsername(String username) {
		return this.userRepository.findById(username).orElseThrow(() -> new ObjectNotFoundException("Usuário não encontrado, converse com um administrador do sistema."));
	}

}

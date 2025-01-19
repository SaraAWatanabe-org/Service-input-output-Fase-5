package com.challenge.fastfood.service_io.services.impl;

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
import org.springframework.stereotype.Service;

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
import com.challenge.fastfood.service_io.entities.UserEntity;
import com.challenge.fastfood.service_io.exceptions.DataIntegrityException;
import com.challenge.fastfood.service_io.exceptions.LoginFailException;
import com.challenge.fastfood.service_io.exceptions.NewPasswordRequiredException;
import com.challenge.fastfood.service_io.repositories.UserRepository;
import com.challenge.fastfood.service_io.services.CognitoService;

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
			 throw new LoginFailException(e.getMessage());
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
	
	public AdminCreateUserResult createUser(String username, String email, String name, String cpf) {
		Optional<UserEntity> userOptional = this.userRepository.findById(username);
		if(userOptional.isPresent()) {
			throw new DataIntegrityException("Já existe um usuário com esse username");
		}
		
        List<AttributeType> attributes = new ArrayList<>();
        attributes.add(new AttributeType().withName("name").withValue(name));
        attributes.add(new AttributeType().withName("email").withValue(email));
        
		AdminCreateUserRequest createUserRequest = new AdminCreateUserRequest()
				.withUserPoolId(userPoolId)
				.withUsername(username)
				.withTemporaryPassword(cpf)
				.withUserAttributes(attributes);
		
		AdminCreateUserResult response = cognitoClient.adminCreateUser(createUserRequest);
		
		
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(response.getUser().getUsername());
        userEntity.setEmail(email);
        userEntity.setName(name);
        userEntity.setCpf(cpf);
        userRepository.save(userEntity);
        
		return response;
	}

	public void addUserToGroup(String username, String groupName) {
		AdminAddUserToGroupRequest addUserToGroupRequest = new AdminAddUserToGroupRequest()
				.withUserPoolId(userPoolId)
				.withUsername(username)
				.withGroupName(groupName);
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
	
}

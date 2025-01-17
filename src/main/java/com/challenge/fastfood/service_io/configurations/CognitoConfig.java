package com.challenge.fastfood.service_io.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.RegionUtils;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;

@Configuration
public class CognitoConfig {

	@Value("${aws.accessKeyId}")
	private String accessKeyId;

	@Value("${aws.secretKey}")
	private String secretKey;

	@Value("${aws.region}")
	private String region;

	@Bean
	AWSCognitoIdentityProvider cognitoClient() {
		return AWSCognitoIdentityProviderClientBuilder.standard()
				.withRegion(RegionUtils.getRegion(region).getName())
				.withCredentials(new AWSStaticCredentialsProvider(
						new BasicAWSCredentials(accessKeyId, secretKey)))
				.build();
	}

}

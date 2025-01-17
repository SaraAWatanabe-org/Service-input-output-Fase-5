package com.challenge.fastfood.service_io.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.RegionUtils;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class AmazonS3Config {

	@Value("${aws.accessKeyId}")
	private String accessKeyId;

	@Value("${aws.secretKey}")
	private String secretKey;

	@Value("${aws.region}")
	private String region;


	public AWSCredentials credentials() {
		AWSCredentials credentials = new BasicAWSCredentials(
				accessKeyId,
				secretKey
				);
		return credentials;
	}

	@Bean
	AmazonS3 s3Client() {
		return AmazonS3ClientBuilder.standard()
				.withRegion(RegionUtils.getRegion(region).getName())
				.withCredentials(new AWSStaticCredentialsProvider(credentials()))
				.build();
	}

}

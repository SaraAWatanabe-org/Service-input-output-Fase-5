package com.challenge.fastfood.service_io.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;

public class SQSConfig {

	@Value("${aws.region}")
	private String region;

	@Bean
	public AmazonSQS amazonSQS() {
		return AmazonSQSClientBuilder.standard()
				.withRegion(region)
				.build();
	}

}

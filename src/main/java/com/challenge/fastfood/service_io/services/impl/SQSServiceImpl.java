package com.challenge.fastfood.service_io.services.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.challenge.fastfood.service_io.entities.VideoRequestEntity;
import com.challenge.fastfood.service_io.exceptions.MessageQueueFailException;
import com.challenge.fastfood.service_io.services.SQSService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SQSServiceImpl implements SQSService {

    @Value("${aws.sqs.queueUrl}")
    private String queueUrl;

    private final AmazonSQS amazonSQS;

    private ObjectMapper objectMapper;

    public SQSServiceImpl(AmazonSQS amazonSQS, ObjectMapper objectMapper) {
        this.amazonSQS = amazonSQS;
        this.objectMapper = objectMapper;
    }

    public void sendMessage(String message) {
        amazonSQS.sendMessage(queueUrl, message);
    }
    
    public void sendMessage(VideoRequestEntity videoRequest) {
        try {
            String message = objectMapper.writeValueAsString(videoRequest);
            amazonSQS.sendMessage(queueUrl, message);
        } catch (Exception e) {
            throw new MessageQueueFailException("Erro ao enviar mensagem para o SQS", e);
        }
    }
    
}

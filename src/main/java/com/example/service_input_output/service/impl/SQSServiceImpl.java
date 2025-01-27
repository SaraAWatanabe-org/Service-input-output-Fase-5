package com.example.service_input_output.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.example.service_input_output.entities.VideoRequestEntity;
import com.example.service_input_output.exceptions.MessageQueueFailException;
import com.example.service_input_output.model.dtos.VideoRequestMessageDto;
import com.example.service_input_output.service.SQSService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SQSServiceImpl implements SQSService {

    @Value("${aws.sqs.producer.queueUrl}")
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
        	VideoRequestMessageDto request = new VideoRequestMessageDto(videoRequest);
            String message = objectMapper.writeValueAsString(request);
            amazonSQS.sendMessage(queueUrl, message);
        } catch (Exception e) {
            throw new MessageQueueFailException("Erro ao enviar mensagem para o SQS", e);
        }
    }
    
}

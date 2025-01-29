package com.example.service_input_output.service.impl;

import com.example.service_input_output.entities.VideoRequestEntity;
import com.example.service_input_output.exceptions.MessageQueueFailException;
import com.example.service_input_output.model.dtos.VideoRequestMessageDto;
import com.example.service_input_output.service.SQSService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Service
public class SQSServiceImpl implements SQSService {

    @Value("${cloud.aws.sqs.producer.queueUrl}")
    private String queueUrl;

    private final SqsAsyncClient amazonSQS;

    private final ObjectMapper objectMapper;

    public SQSServiceImpl(SqsAsyncClient amazonSQS, ObjectMapper objectMapper) {
        this.amazonSQS = amazonSQS;
        this.objectMapper = objectMapper;
    }

    public void sendMessage(VideoRequestEntity videoRequest) {
        try {
        	VideoRequestMessageDto request = new VideoRequestMessageDto(videoRequest);
            String message = objectMapper.writeValueAsString(request);
            SendMessageRequest sendMessageRequest = software.amazon.awssdk.services.sqs.model.SendMessageRequest.builder().queueUrl(queueUrl).messageBody(message).build();
            amazonSQS.sendMessage(sendMessageRequest);
        } catch (Exception e) {
            throw new MessageQueueFailException("Erro ao enviar mensagem para o SQS", e);
        }
    }
    
}

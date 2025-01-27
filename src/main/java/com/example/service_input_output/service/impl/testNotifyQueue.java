package com.example.service_input_output.service.impl;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class testNotifyQueue {

    private final AmazonSQS amazonSQS;

    @Value("${aws.sqs.listener.queueUrl}")
    private String queueUrl;

    public testNotifyQueue(AmazonSQS amazonSQS) {
        this.amazonSQS = amazonSQS;
    }

    public void sendMessage(String message) {
        SendMessageRequest sendMessageRequest = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageBody(message);
        amazonSQS.sendMessage(sendMessageRequest);
        System.out.println("Mensagem enviada para fila soat_notifier: " + message);
    }

    public void testConnection(AmazonSQS amazonSQS, @Value("${aws.sqs.listener.queueUrl}") String queueUrl) {
        amazonSQS.receiveMessage(queueUrl).getMessages().forEach(msg -> {
            System.out.println("Message: " + msg.getBody());
        });
    }
}

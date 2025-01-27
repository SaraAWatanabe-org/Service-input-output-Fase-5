package com.example.service_input_output.queue;

import com.example.service_input_output.model.dtos.NotifyRequest;
import com.example.service_input_output.service.impl.OutputService;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class NotificationListener {

    final OutputService outputService;

    @SqsListener("${aws.sqs.additionalQueue.queueName}")
    public void processMessage(NotifyRequest notifyRequest) {
        System.out.println("Message received from Notify queue " + notifyRequest);
        outputService.processNotification(notifyRequest);
    }

}

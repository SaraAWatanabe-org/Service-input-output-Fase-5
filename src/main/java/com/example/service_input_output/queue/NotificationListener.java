package com.example.service_input_output.queue;

import com.example.service_input_output.service.impl.OutputService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationListener {

    final OutputService outputService;

    @Value("${aws.sqs.listener.queueName}")
    private String queueName;

    @PostConstruct
    public void init() {
        System.out.println("NotificationListener initialized for queue: " + queueName);
    }

//    @SqsListener("${aws.sqs.listener.queueName}")
    @SqsListener("soat_notifier")
    public void processMessage(String notifyRequest) {
//    public void processMessage(NotifyRequest notifyRequest) {
        System.out.println("Message received from Notify queue " + notifyRequest);
        System.out.println("Message received from Notify queue " + notifyRequest);
        System.out.println("Message received from Notify queue " + notifyRequest);
//        outputService.processNotification(notifyRequest);
    }

}

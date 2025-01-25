package com.example.service_input_output.queue;

import com.example.service_input_output.model.dto.NotifyRequest;
import com.example.service_input_output.service.OutputService;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class NotificationListener {

    final OutputService outputService;

    @SqsListener("${cloud.aws.sqs.queue-name}")
    public void processMessage(NotifyRequest notifyRequest) throws IOException {
        System.out.println("Message received from Notify queue " + notifyRequest);
        outputService.processNotification(notifyRequest);
    }

}

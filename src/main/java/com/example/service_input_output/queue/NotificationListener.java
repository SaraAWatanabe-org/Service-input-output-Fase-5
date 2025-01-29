package com.example.service_input_output.queue;

import com.example.service_input_output.model.dtos.NotifyRequest;
import com.example.service_input_output.service.impl.OutputService;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationListener {

    final OutputService outputService;

    @SqsListener("soat_notifier")
    public void processMessage(NotifyRequest notifyRequest) {
        log.info("Message received from Notify queue {}", notifyRequest);
        outputService.processNotification(notifyRequest);
    }

}

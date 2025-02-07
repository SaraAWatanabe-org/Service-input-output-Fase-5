package com.example.service_input_output.service.impl;

import com.example.service_input_output.enums.NotificationType;
import com.example.service_input_output.model.dtos.NotifyRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class OutputServiceTest {

    @InjectMocks
    OutputService outputService;

    @Mock
    EmailService emailService;

    @Mock
    VideoRequestServiceImpl videoRequestService;

    @Test
    void processSucess() {
        var request = getNotifyRequest(NotificationType.SUCCESS);

        assertDoesNotThrow(() -> {
            var response = outputService.processNotification(request);
            assertEquals(NotificationType.SUCCESS.name(), response.message());
        });
    }

    @Test
    void processError() {
        var request = getNotifyRequest(NotificationType.ERROR);

        assertDoesNotThrow(() -> {
            var response = outputService.processNotification(request);
            assertEquals(NotificationType.ERROR.name(), response.message());
        });
    }

    private NotifyRequest getNotifyRequest(NotificationType notificationType) {
        return new NotifyRequest(
                "teste@test.com",
                notificationType,
                "address",
                UUID.randomUUID()
        );
    }
}
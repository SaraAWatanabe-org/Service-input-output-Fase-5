package com.example.service_input_output.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.example.service_input_output.model.dtos.NotifyRequest;
import com.example.service_input_output.model.dtos.NotifyResponse;

import java.io.IOException;

import static com.example.service_input_output.utils.Constants.EmailConstants.*;
import static java.lang.String.format;
import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class OutputService {

    final private EmailService emailService;

    public NotifyResponse processNotification(NotifyRequest notifyRequest) throws IOException {
        if (isNull(notifyRequest) || isNull(notifyRequest.notificationType())) {
            throw new IllegalArgumentException("NotifyRequest or NotificationType cannot be null");
        }

        return switch (notifyRequest.notificationType()) {
            case SUCCESS -> processSuccess(notifyRequest);
            case ERROR -> processError(notifyRequest);
        };
    }

    private NotifyResponse processSuccess(NotifyRequest notifyRequest) {
        emailService.sendSimpleEmail(notifyRequest.email(), SUBJECT, format(SUCCESS_MESSAGE, notifyRequest.bucketAddress()));
        return new NotifyResponse("SUCCESS");
    }

    private NotifyResponse processError(NotifyRequest notifyRequest) {
        emailService.sendSimpleEmail(notifyRequest.email(), SUBJECT, ERROR_MESSAGE);
        return new NotifyResponse("ERROR");
    }
}

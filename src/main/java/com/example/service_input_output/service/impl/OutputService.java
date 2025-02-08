package com.example.service_input_output.service.impl;

import com.example.service_input_output.enums.NotificationType;
import com.example.service_input_output.enums.RequestStatusEnum;
import com.example.service_input_output.model.dtos.NotifyRequest;
import com.example.service_input_output.model.dtos.NotifyResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.example.service_input_output.utils.Constants.EmailConstants.*;
import static java.lang.String.format;
import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutputService {

    final private EmailService emailService;
    final private VideoRequestServiceImpl videoRequestService;

    public NotifyResponse processNotification(NotifyRequest notifyRequest) {
        if (isNull(notifyRequest) || isNull(notifyRequest.notificationType())) {
            throw new IllegalArgumentException("NotifyRequest and NotificationType cannot be null");
        }

        return switch (notifyRequest.notificationType()) {
            case SUCCESS -> processSuccess(notifyRequest);
            case ERROR -> processError(notifyRequest);
        };
    }

    private NotifyResponse processSuccess(NotifyRequest notifyRequest) {
        emailService.sendSimpleEmail(notifyRequest.email(), SUBJECT, format(SUCCESS_MESSAGE, notifyRequest.bucketAddress()));
        
        videoRequestService.updateVideoRequest(notifyRequest.videoId(), RequestStatusEnum.FINISHED, notifyRequest.bucketAddress());
        
        return new NotifyResponse(NotificationType.SUCCESS.name());
    }

    private NotifyResponse processError(NotifyRequest notifyRequest) {
        emailService.sendSimpleEmail(notifyRequest.email(), SUBJECT, ERROR_MESSAGE);
        videoRequestService.updateVideoRequest(notifyRequest.videoId(), RequestStatusEnum.ERROR, null);
        return new NotifyResponse(NotificationType.ERROR.name());
    }
}

package com.example.service_input_output.model.dtos;

import com.example.service_input_output.enums.NotificationType;

import java.util.UUID;

public record NotifyRequest(
        String email,
        NotificationType notificationType,
        String bucketAddress,
        UUID videoId
) {
}

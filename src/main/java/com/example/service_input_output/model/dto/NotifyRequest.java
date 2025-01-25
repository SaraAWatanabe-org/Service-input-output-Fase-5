package com.example.service_input_output.model.dto;

import com.example.service_input_output.enums.NotificationType;

public record NotifyRequest(
        String email,
        NotificationType notificationType,
        String bucketAddress
) {
}

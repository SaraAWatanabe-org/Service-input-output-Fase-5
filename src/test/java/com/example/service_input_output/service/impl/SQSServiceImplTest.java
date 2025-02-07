package com.example.service_input_output.service.impl;

import com.example.service_input_output.entities.VideoRequestEntity;
import com.example.service_input_output.exceptions.MessageQueueFailException;
import com.example.service_input_output.model.dtos.VideoRequestMessageDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;
import software.amazon.awssdk.thirdparty.jackson.core.JsonProcessingException;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SQSServiceImplTest {

    @Mock
    SqsAsyncClient amazonSQS;

    @Spy
    ObjectMapper objectMapper;

    @Mock
    CompletableFuture<SendMessageResponse> sendMessageResponse;

    @InjectMocks
    SQSServiceImpl sqsService;

    private final String queueUrl = "test-queue-url";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(sqsService, "queueUrl", queueUrl);
    }

    @Test
    void shouldSendMessageSuccessfully() throws JsonProcessingException, com.fasterxml.jackson.core.JsonProcessingException {
        VideoRequestEntity videoRequest = mock(VideoRequestEntity.class);
        VideoRequestMessageDto requestDto = new VideoRequestMessageDto(videoRequest);
        String messageBody = "{\"videoId\":123}";

        when(objectMapper.writeValueAsString(any())).thenReturn(messageBody);

        assertDoesNotThrow(() -> sqsService.sendMessage(videoRequest));
    }

    @Test
    void shouldThrowExceptionWhenMessageSerializationFails() throws JsonProcessingException, com.fasterxml.jackson.core.JsonProcessingException {
        VideoRequestEntity videoRequest = mock(VideoRequestEntity.class);

        when(objectMapper.writeValueAsString(any(VideoRequestMessageDto.class)))
                .thenThrow(new RuntimeException("Serialization error"));

        assertThrows(MessageQueueFailException.class, () -> sqsService.sendMessage(videoRequest));
        verify(amazonSQS, never()).sendMessage(any(SendMessageRequest.class));
    }
}

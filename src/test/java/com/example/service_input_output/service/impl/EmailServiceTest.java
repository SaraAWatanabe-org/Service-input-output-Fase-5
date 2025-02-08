package com.example.service_input_output.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    private final String recipient = "noreply@example.com";

    @BeforeEach
    void setUp() throws Exception {
        emailService = new EmailService(mailSender);
    }

    @Test
    void shouldSendSimpleEmailSuccessfully() {
        String to = "test@example.com";
        String subject = "Test Subject";
        String text = "Test Message";
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        emailService.sendSimpleEmail(to, subject, text);

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void shouldHandleExceptionWhenSendingEmail() {
        String to = "test@example.com";
        String subject = "Test Subject";
        String text = "Test Message";
        doThrow(new RuntimeException("Mail sending failed"))
                .when(mailSender).send(any(SimpleMailMessage.class));

        emailService.sendSimpleEmail(to, subject, text);

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}

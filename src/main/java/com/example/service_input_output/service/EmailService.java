package com.example.service_input_output.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class EmailService {

    @Value("${mail.recipient}")
    private String recipient;

    final JavaMailSender mailSender;

    public void sendSimpleEmail(String to, String subject, String text) {
        final SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(recipient);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        try {
            mailSender.send(message);
            log.info("Email successfully sent to: {}", to);
        } catch (Exception e) {
            log.error("Error while sending email: {}", e.getMessage());
        }
    }
}
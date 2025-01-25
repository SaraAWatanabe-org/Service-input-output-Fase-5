package com.example.service_input_output.controller;

import com.example.service_input_output.model.dto.NotifyRequest;
import com.example.service_input_output.model.dto.NotifyResponse;
import com.example.service_input_output.service.OutputService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/notify")
@RequiredArgsConstructor
public class NotificationController {

    final private OutputService outputService;

    @PostMapping
    public ResponseEntity<NotifyResponse> notify(@RequestBody NotifyRequest request) throws IOException {
        var response = outputService.processNotification(request);
        return ResponseEntity.ok(response);
    }
}

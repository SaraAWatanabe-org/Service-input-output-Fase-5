package com.challenge.fastfood.service_io.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.challenge.fastfood.service_io.entities.VideoRequestEntity;
import com.challenge.fastfood.service_io.exceptions.DataIntegrityException;
import com.challenge.fastfood.service_io.services.VideoRequestService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/videos")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Video", description = "Video Controller")
public class VideoController {

    private final VideoRequestService videoRequestService;

    public VideoController(VideoRequestService videoRequestService) {
        this.videoRequestService = videoRequestService;
    }

    @PostMapping("/upload")
    @Operation(summary = "Create Video Request", description = "Create a video requesto to make images")
    public ResponseEntity<VideoRequestEntity> uploadVideo(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty() || !file.getContentType().startsWith("video/")) {
        	throw new DataIntegrityException("Arquivo inválido. Envie um vídeo.");
        }

        var videoRequest = videoRequestService.createVideoRequest(file);
        return new ResponseEntity<>(videoRequest, HttpStatus.CREATED);
    }
    
}

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

@RestController
@RequestMapping("/videos")
@CrossOrigin(origins = "*", maxAge = 3600)
public class VideoController {

    private final VideoRequestService videoRequestService;

    public VideoController(VideoRequestService videoRequestService) {
        this.videoRequestService = videoRequestService;
    }

    @PostMapping("/upload")
    public ResponseEntity<VideoRequestEntity> uploadVideo(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty() || !file.getContentType().startsWith("video/")) {
        	throw new DataIntegrityException("Arquivo inválido. Envie um vídeo.");
        }

        var videoRequest = videoRequestService.createVideoRequest(file);
        return new ResponseEntity<>(videoRequest, HttpStatus.CREATED);
    }
    
}

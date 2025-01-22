package com.challenge.fastfood.service_io.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.challenge.fastfood.service_io.dtos.VideoRequestDto;
import com.challenge.fastfood.service_io.dtos.VideoRequestSimpleDto;
import com.challenge.fastfood.service_io.entities.VideoRequestEntity;
import com.challenge.fastfood.service_io.exceptions.DataIntegrityException;
import com.challenge.fastfood.service_io.security.JwtUtils;
import com.challenge.fastfood.service_io.services.S3Service;
import com.challenge.fastfood.service_io.services.VideoRequestService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/videos")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Video", description = "Video Controller")
public class VideoController {

    private final VideoRequestService videoRequestService;
    
    private final JwtUtils jwtUtils;
    
    private final S3Service s3Service;

    public VideoController(VideoRequestService videoRequestService, S3Service s3Service, JwtUtils jwtUtils) {
        this.videoRequestService = videoRequestService;
        this.s3Service = s3Service;
        this.jwtUtils = jwtUtils;
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
    
    @GetMapping
    @Operation(summary = "Get VideoRequest Page", description = "Get a Page of VideoRequest")
    public ResponseEntity<Page<VideoRequestSimpleDto>> getVideoRequests(@PageableDefault(page = 0, size = 24, sort = "createDate", direction = Direction.ASC) Pageable pageable) {
    	String email = jwtUtils.getEmailFromJwt();
    	Page<VideoRequestEntity> page = videoRequestService.findAllByUser(pageable, email);
		Page<VideoRequestSimpleDto> pageDto = page.map(videoRequestEntity -> new VideoRequestSimpleDto(videoRequestEntity));
		return ResponseEntity.ok().body(pageDto);
    }
    
    @GetMapping("/{id}/temporary-url")
    @Operation(summary = "Get by Id with Token", description = "Get by Id with S3 temporary Url")
    public ResponseEntity<VideoRequestDto> getByIdWithTemporaryUrl(UUID id) {
    	String email = jwtUtils.getEmailFromJwt();
    	VideoRequestEntity videoRequestEntity = videoRequestService.findByIdAndUserEmail(id, email);
    	VideoRequestDto videoRequestWithTempUrl = s3Service.generatePresignedUrl(videoRequestEntity);
		return ResponseEntity.ok().body(videoRequestWithTempUrl);
    }
    
}

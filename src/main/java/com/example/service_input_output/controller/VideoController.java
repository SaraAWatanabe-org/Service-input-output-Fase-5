package com.example.service_input_output.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.service_input_output.entities.VideoRequestEntity;
import com.example.service_input_output.exceptions.DataIntegrityException;
import com.example.service_input_output.model.dtos.VideoRequestDto;
import com.example.service_input_output.model.dtos.VideoRequestSimpleDto;
import com.example.service_input_output.security.JwtUtils;
import com.example.service_input_output.service.S3Service;
import com.example.service_input_output.service.VideoRequestService;

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
    public ResponseEntity<VideoRequestDto> uploadVideo(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty() || !file.getContentType().startsWith("video/")) {
        	throw new DataIntegrityException("Arquivo inválido. Envie um vídeo.");
        }

        VideoRequestEntity videoRequestEntity = videoRequestService.createVideoRequest(file);
        VideoRequestDto videoRequest = new VideoRequestDto(videoRequestEntity);
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
    
    @GetMapping("/{id}/temporary-zip-url")
    @Operation(summary = "Get by Id with Token", description = "Get by Id with S3 temporary Url")
    public ResponseEntity<VideoRequestDto> getByIdWithTemporaryUrl(@PathVariable UUID id) {
    	String email = jwtUtils.getEmailFromJwt();
    	VideoRequestEntity videoRequestEntity = videoRequestService.findByIdAndUserEmail(id, email);
    	VideoRequestDto videoRequestWithTempUrl = s3Service.generatePresignedUrl(videoRequestEntity);
		return ResponseEntity.ok().body(videoRequestWithTempUrl);
    }
    
}

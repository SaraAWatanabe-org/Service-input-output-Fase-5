package com.example.service_input_output.service.impl;

import com.example.service_input_output.entities.UserEntity;
import com.example.service_input_output.entities.VideoRequestEntity;
import com.example.service_input_output.enums.RequestStatusEnum;
import com.example.service_input_output.exceptions.FileUploadFailException;
import com.example.service_input_output.model.dtos.UploadS3Response;
import com.example.service_input_output.repositories.UserRepository;
import com.example.service_input_output.repositories.VideoRequestRepository;
import com.example.service_input_output.service.S3Service;
import com.example.service_input_output.service.SQSService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VideoRequestServiceTest {

    @Spy
    private VideoRequestRepository videoRequestRepository;

    @Spy
    private UserRepository userRepository;

    @Spy
    private S3Service s3Service;

    @Mock
    private SQSService sqsService;

    @Mock
    private MultipartFile file;

    @InjectMocks
    private VideoRequestServiceImpl videoRequestService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
    }


    @Test
    void shouldCreateVideoRequestSuccessfully() throws IOException {
        UserEntity user = new UserEntity();
        user.setEmail("test@example.com");

        UploadS3Response uploadResponse = new UploadS3Response("https://s3.amazonaws.com/test-file", "test-file-key");
        VideoRequestEntity requestEntity = new VideoRequestEntity();
        requestEntity.setRequester(user);
        requestEntity.setUrl(uploadResponse.getUrl());
        requestEntity.setObjectKey(uploadResponse.getKey());
        requestEntity.setStatus(RequestStatusEnum.WAITING_PROCESS);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        Jwt jwt = mock(Jwt.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(jwt);
        when(jwt.getClaim("email")).thenReturn(user.getEmail());
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        when(s3Service.uploadFile(file, user.getEmail())).thenReturn(uploadResponse);

        SecurityContextHolder.setContext(securityContext);

        assertDoesNotThrow(() -> videoRequestService.createVideoRequest(file));
    }

    @Test
    void shouldThrowFileUploadFailExceptionWhenUploadFails() throws IOException {
        UserEntity user = new UserEntity();
        user.setEmail("test@example.com");

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        Jwt jwt = mock(Jwt.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(jwt);
        when(jwt.getClaim("email")).thenReturn(user.getEmail());
        when(userRepository.findById(user.getEmail())).thenReturn(Optional.of(user));
        when(s3Service.uploadFile(file, user.getEmail())).thenThrow(new IOException("Upload failed"));

        SecurityContextHolder.setContext(securityContext);

        assertThrows(FileUploadFailException.class, () -> videoRequestService.createVideoRequest(file));
    }

    @Test
    void shouldFindAllByUser() {
        Pageable pageable = mock(Pageable.class);
        String email = "test@example.com";

        assertDoesNotThrow(() -> videoRequestService.findAllByUser(pageable, email));
    }

    @Test
    void updateStatus() {
        var status = mock(RequestStatusEnum.class);
        var uuid = mock(UUID.class);

        when(videoRequestRepository.findById(any())).thenReturn(Optional.of(new VideoRequestEntity()));
        when(videoRequestRepository.save(any())).thenReturn(new VideoRequestEntity());

        assertDoesNotThrow(() -> videoRequestService.updateStatus(uuid, status));
    }

    @Test
    void findByIdAndUserEmail() {
        var uuid = mock(UUID.class);
        var email = "test@example.com";

        when(videoRequestRepository.findByIdAndRequesterEmail(any(), any())).thenReturn(Optional.of(new VideoRequestEntity()));

        assertDoesNotThrow(() -> videoRequestService.findByIdAndUserEmail(uuid, email));
    }
}

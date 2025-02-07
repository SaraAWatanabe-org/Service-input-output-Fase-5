package com.example.service_input_output.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.service_input_output.entities.VideoRequestEntity;
import com.example.service_input_output.model.dtos.UploadS3Response;
import com.example.service_input_output.model.dtos.VideoRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class S3ServiceImplTest {

    @Spy
    AmazonS3 s3Client;

    @Mock
    MultipartFile file;

    @Mock
    URL url;

    @InjectMocks
    S3ServiceImpl s3Service;

    private final String bucketName = "test-bucket";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(s3Service, "bucketName", "test-bucket");
    }

    @Test
    void shouldUploadFileSuccessfully() throws IOException {
        String email = "test@example.com";
        String fileName = "test-file.txt";
        String expectedUrl = "https://s3.amazonaws.com/test-bucket/test-file.txt";

        when(file.getOriginalFilename()).thenReturn(fileName);
        when(file.getSize()).thenReturn(100L);
        when(file.getContentType()).thenReturn("text/plain");

        doReturn(new URL(expectedUrl)).when(s3Client).getUrl(any(), any());

        System.out.println("Mocked bucketName: " + bucketName);
        System.out.println("Mocked fileName: " + fileName);
        System.out.println("Mocked URL: " + s3Client.getUrl(bucketName, fileName));

        UploadS3Response response = s3Service.uploadFile(file, email);

        assertNotNull(response);
        assertEquals(expectedUrl, response.getUrl());
        verify(s3Client, times(1)).putObject(eq(bucketName), anyString(), any(), any(ObjectMetadata.class));
    }

    @Test
    void shouldHandleNullUrlGracefully() throws IOException {
        String email = "test@example.com";
        String fileName = "test-file.txt";

        when(file.getOriginalFilename()).thenReturn(fileName);
        when(file.getSize()).thenReturn(100L);
        when(file.getContentType()).thenReturn("text/plain");
        when(s3Client.getUrl(eq(bucketName), anyString())).thenReturn(null);

        assertThrows(NullPointerException.class, () -> s3Service.uploadFile(file, email));
        verify(s3Client, times(1)).putObject(eq(bucketName), anyString(), any(), any(ObjectMetadata.class));
    }

    @Test
    void shouldGeneratePresignedUrlSuccessfully() {
        VideoRequestEntity videoRequestEntity = mock(VideoRequestEntity.class);
        String objectKey = "video.mp4";
        String expectedUrl = "https://s3.amazonaws.com/test-bucket/video.mp4";

        when(videoRequestEntity.getObjectKey()).thenReturn(objectKey);
        when(s3Client.generatePresignedUrl(any(GeneratePresignedUrlRequest.class)))
                .thenReturn(mockValidUrl(expectedUrl));

        VideoRequestDto response = s3Service.generatePresignedUrl(videoRequestEntity);

        assertNotNull(response);
        assertEquals(expectedUrl, response.getUrl());
        verify(s3Client, times(1)).generatePresignedUrl(any(GeneratePresignedUrlRequest.class));
    }

    private URL mockValidUrl(String urlString) {
        try {
            URL url = new URL(urlString);
            System.out.println("Mocking URL: " + url);
            return url;
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid URL format", e);
        }
    }
}

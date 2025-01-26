package com.example.service_input_output.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.example.service_input_output.entities.VideoRequestEntity;
import com.example.service_input_output.model.dtos.UploadS3Response;
import com.example.service_input_output.model.dtos.VideoRequestDto;

public interface S3Service {

	UploadS3Response uploadFile(MultipartFile file, String email) throws IOException;
	
	VideoRequestDto generatePresignedUrl(VideoRequestEntity videoRequestEntity);

}

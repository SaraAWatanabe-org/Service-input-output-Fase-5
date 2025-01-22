package com.challenge.fastfood.service_io.services;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.challenge.fastfood.service_io.dtos.UploadS3Response;
import com.challenge.fastfood.service_io.dtos.VideoRequestDto;
import com.challenge.fastfood.service_io.entities.VideoRequestEntity;

public interface S3Service {

	UploadS3Response uploadFile(MultipartFile file, String email) throws IOException;
	
	VideoRequestDto generatePresignedUrl(VideoRequestEntity videoRequestEntity);

}

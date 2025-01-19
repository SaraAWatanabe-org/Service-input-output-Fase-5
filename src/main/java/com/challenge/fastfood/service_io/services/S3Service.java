package com.challenge.fastfood.service_io.services;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.challenge.fastfood.service_io.dtos.UploadS3Response;

public interface S3Service {

	UploadS3Response uploadFile(MultipartFile image) throws IOException;

}

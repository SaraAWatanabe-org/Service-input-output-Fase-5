package com.challenge.fastfood.service_io.services.impl;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.challenge.fastfood.service_io.services.S3Service;

@Service
public class S3ServiceImpl implements S3Service {

	private final AmazonS3 s3Client;
	
	@Value("${aws.s3.bucketName}") 
	private String bucketName;

	private final String folderName = "public/videos";

	public S3ServiceImpl(AmazonS3 s3Client) {
		this.s3Client = s3Client;
	}

	public String uploadFile(MultipartFile file) throws IOException {
		String fileName = folderName + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(file.getSize());
		metadata.setContentType(file.getContentType());

		s3Client.putObject(bucketName, fileName, file.getInputStream(), metadata);
		return s3Client.getUrl(bucketName, fileName).toString();
	}

}

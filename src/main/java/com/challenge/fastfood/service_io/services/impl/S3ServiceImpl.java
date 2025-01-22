package com.challenge.fastfood.service_io.services.impl;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.challenge.fastfood.service_io.dtos.UploadS3Response;
import com.challenge.fastfood.service_io.dtos.VideoRequestDto;
import com.challenge.fastfood.service_io.entities.VideoRequestEntity;
import com.challenge.fastfood.service_io.services.S3Service;

@Service
public class S3ServiceImpl implements S3Service {

	@Value("${aws.s3.bucketName}") 
	private String bucketName;

	private final AmazonS3 s3Client;

	public S3ServiceImpl(AmazonS3 s3Client) {
		this.s3Client = s3Client;
	}

	@Override
	public UploadS3Response uploadFile(MultipartFile file, String email) throws IOException {
		String fileName = this.buildS3Key(email, file);
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(file.getSize());
		metadata.setContentType(file.getContentType());

		s3Client.putObject(bucketName, fileName, file.getInputStream(), metadata);
		String url = s3Client.getUrl(bucketName, fileName).toString();
		return new UploadS3Response(url, fileName);
	}

	@Override
	public VideoRequestDto generatePresignedUrl(VideoRequestEntity videoRequestEntity) {
		Date expiration = new Date();
		long expirationTimeMillis = expiration.getTime() + (1000 * 60 * 15);
		expiration.setTime(expirationTimeMillis);

		GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(
				this.bucketName,
				videoRequestEntity.getObjectKey())
				.withMethod(com.amazonaws.HttpMethod.GET)
				.withExpiration(expiration);

		VideoRequestDto videoRequest = new VideoRequestDto(videoRequestEntity);
		URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
		videoRequest.setUrl(url.toString());

		return videoRequest;
	}

	private String buildS3Key(String email, MultipartFile file) {
		LocalDate now = LocalDate.now();
		return String.format("%s/%d/%02d/%02d/%s", email, now.getYear(), now.getMonthValue(), now.getDayOfMonth(), file.getOriginalFilename());
	}

}

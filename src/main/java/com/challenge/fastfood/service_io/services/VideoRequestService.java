package com.challenge.fastfood.service_io.services;

import org.springframework.web.multipart.MultipartFile;

import com.challenge.fastfood.service_io.entities.VideoRequestEntity;

public interface VideoRequestService {

	public VideoRequestEntity createVideoRequest(MultipartFile file);

}

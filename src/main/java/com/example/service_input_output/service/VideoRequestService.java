package com.example.service_input_output.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.example.service_input_output.entities.VideoRequestEntity;

public interface VideoRequestService {

	public VideoRequestEntity createVideoRequest(MultipartFile file);

	public Page<VideoRequestEntity> findAllByUser(Pageable pageable, String email);

	public VideoRequestEntity findByIdAndUserEmail(UUID id, String email);

}

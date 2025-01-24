package com.challenge.fastfood.service_io.services.impl;

import java.io.IOException;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.challenge.fastfood.service_io.dtos.UploadS3Response;
import com.challenge.fastfood.service_io.entities.UserEntity;
import com.challenge.fastfood.service_io.entities.VideoRequestEntity;
import com.challenge.fastfood.service_io.enums.RequestStatusEnum;
import com.challenge.fastfood.service_io.exceptions.FileUploadFailException;
import com.challenge.fastfood.service_io.exceptions.ObjectNotFoundException;
import com.challenge.fastfood.service_io.repositories.UserRepository;
import com.challenge.fastfood.service_io.repositories.VideoRequestRepository;
import com.challenge.fastfood.service_io.services.S3Service;
import com.challenge.fastfood.service_io.services.SQSService;
import com.challenge.fastfood.service_io.services.VideoRequestService;
import com.challenge.fastfood.service_io.specifications.VideoRequestSpecification;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class VideoRequestServiceImpl implements VideoRequestService {

	private final VideoRequestRepository videoRequestRepository;  

	private final UserRepository userRepository;

	private final S3Service s3Service;

	private final SQSService sqsService;

	public VideoRequestServiceImpl(VideoRequestRepository videoRequestRepository, UserRepository userRepository, S3Service s3Service, SQSService sqsService) {
		this.videoRequestRepository = videoRequestRepository;
		this.userRepository = userRepository;
		this.s3Service = s3Service;
		this.sqsService = sqsService;
	}

	@Transactional
	public VideoRequestEntity createVideoRequest(MultipartFile file) {
		UserEntity user = getLoggedUser();

		VideoRequestEntity request = new VideoRequestEntity();

		UploadS3Response uploadResponse = null;
		try {
			uploadResponse = s3Service.uploadFile(file, user.getEmail());
		} catch (IOException e) {
			throw new FileUploadFailException("Falha para subir aquivo no S3.");
		}

		request.setRequester(user);
		request.setUrl(uploadResponse.getUrl());
		request.setObjectKey(uploadResponse.getKey());
		request.setStatus(RequestStatusEnum.WAITING_PROCESS);
		request = this.videoRequestRepository.save(request);
		
		sqsService.sendMessage(request);

		return request;
	}

	public VideoRequestEntity updateStatus(UUID id, RequestStatusEnum status)  {
		VideoRequestEntity videoRequestEntity = findById(id);
		videoRequestEntity.setStatus(status);
		return this.videoRequestRepository.save(videoRequestEntity);
	}

	public VideoRequestEntity findById(UUID id) {
		return this.videoRequestRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("VideoRequest not found. Id: " + id));
	}
	
	@Override
	public Page<VideoRequestEntity> findAllByUser(Pageable pageable, String email) {
		return videoRequestRepository.findAll(VideoRequestSpecification.byEmail(email), pageable);
	}
	
	@Override
	public VideoRequestEntity findByIdAndUserEmail(UUID id, String email) {
		return this.videoRequestRepository.findByIdAndRequesterEmail(id, email).orElseThrow(() -> new ObjectNotFoundException("Request not found or not belonging to the user."));
	}

	private UserEntity getLoggedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Jwt jwt = (Jwt) authentication.getPrincipal();
		String email = jwt.getClaim("email");

		return findUserByUsername(email);
	}

	private UserEntity findUserByUsername(String username) {
		return this.userRepository.findById(username).orElseThrow(() -> new ObjectNotFoundException("Usuário não encontrado, converse com um administrador do sistema."));
	}

}

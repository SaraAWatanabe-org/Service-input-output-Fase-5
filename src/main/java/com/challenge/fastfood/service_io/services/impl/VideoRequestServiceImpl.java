package com.challenge.fastfood.service_io.services.impl;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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

		String fileUrl = null;
		try {
			fileUrl = s3Service.uploadFile(file);
		} catch (IOException e) {
			throw new FileUploadFailException("Falha para subir aquivo no S3.");
		}
		
		request.setRequester(user);
		request.setUrl(fileUrl);
		request.setStatus(RequestStatusEnum.WAITING_PROCESS);
		request = this.videoRequestRepository.save(request);
		
        sqsService.sendMessage(request);
        
		return request;
	}
	
	
	private UserEntity getLoggedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Jwt jwt = (Jwt) authentication.getPrincipal();
		String username = jwt.getClaim("username");
		
		return findUserByUsername(username);
	}

	private UserEntity findUserByUsername(String username) {
		return this.userRepository.findById(username).orElseThrow(() -> new ObjectNotFoundException("Usuário não encontrado, converse com um administrador do sistema."));
	}

}

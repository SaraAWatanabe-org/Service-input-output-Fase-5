package com.example.service_input_output.model.dtos;

import java.util.UUID;

import com.example.service_input_output.entities.VideoRequestEntity;

import lombok.Data;

@Data
public class VideoRequestMessageDto {

	private UUID id;
	private String email;
	private String url;
	private String key;

	public VideoRequestMessageDto(VideoRequestEntity videoRequestEntity) {
		this.id = videoRequestEntity.getId();
		this.url =  videoRequestEntity.getUrl();
		this.key = videoRequestEntity.getObjectKey();
		
		if(videoRequestEntity.getRequester() != null) {
			this.email = videoRequestEntity.getRequester().getEmail();
		}
	}

}

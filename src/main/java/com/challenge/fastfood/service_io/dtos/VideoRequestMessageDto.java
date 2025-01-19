package com.challenge.fastfood.service_io.dtos;

import java.util.UUID;

import com.challenge.fastfood.service_io.entities.VideoRequestEntity;

import lombok.Data;

@Data
public class VideoRequestMessageDto {

	private UUID id;
	private String username;
	private String url;

	public VideoRequestMessageDto(VideoRequestEntity videoRequestEntity) {
		this.id = videoRequestEntity.getId();
		this.url =  videoRequestEntity.getUrl();
		
		if(videoRequestEntity.getRequester() != null) {
			this.username = videoRequestEntity.getRequester().getName();
		}
	}

}

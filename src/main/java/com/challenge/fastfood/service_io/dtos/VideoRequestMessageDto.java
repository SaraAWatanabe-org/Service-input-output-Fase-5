package com.challenge.fastfood.service_io.dtos;

import com.challenge.fastfood.service_io.entities.VideoRequestEntity;

import lombok.Data;

@Data
public class VideoRequestMessageDto {

	private String username;
	private String videoUrl;


	public VideoRequestMessageDto(VideoRequestEntity videoRequestEntity) {
		if(videoRequestEntity.getRequester() != null) {
			this.username = videoRequestEntity.getRequester().getName();
		}
		this.videoUrl =  videoRequestEntity.getUrl();
	}

}

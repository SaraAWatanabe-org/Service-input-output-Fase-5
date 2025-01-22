package com.challenge.fastfood.service_io.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.BeanUtils;

import com.challenge.fastfood.service_io.entities.VideoRequestEntity;

import lombok.Data;

@Data
public class VideoRequestDto {

	private UUID id;
	private String status;
	private String url;
	private String objectKey;
	private LocalDateTime createDate;
	private LocalDateTime lastUpdate;

	public VideoRequestDto() {
	}

	public VideoRequestDto(VideoRequestEntity videoRequestEntity) {
		BeanUtils.copyProperties(videoRequestEntity, this);
	}

}

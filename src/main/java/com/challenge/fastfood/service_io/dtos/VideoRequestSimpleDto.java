package com.challenge.fastfood.service_io.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.BeanUtils;

import com.challenge.fastfood.service_io.entities.VideoRequestEntity;

import lombok.Data;

@Data
public class VideoRequestSimpleDto {

	private UUID id;
	private String status;
	private String objectKey;
	private LocalDateTime createDate;
	private LocalDateTime lastUpdate;

	public VideoRequestSimpleDto() {
	}

	public VideoRequestSimpleDto(VideoRequestEntity videoRequestEntity) {
		BeanUtils.copyProperties(videoRequestEntity, this);
	}

}

package com.example.service_input_output.model.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.BeanUtils;

import com.example.service_input_output.entities.VideoRequestEntity;

import lombok.Data;

@Data
public class VideoRequestDto {

	private UUID id;
	private String status;
	private String zipUrl;
	private String objectKey;
	private LocalDateTime createDate;
	private LocalDateTime lastUpdate;

	public VideoRequestDto() {
	}

	public VideoRequestDto(VideoRequestEntity videoRequestEntity) {
		BeanUtils.copyProperties(videoRequestEntity, this);
		this.status = videoRequestEntity.getStatus().getCode();
	}

}

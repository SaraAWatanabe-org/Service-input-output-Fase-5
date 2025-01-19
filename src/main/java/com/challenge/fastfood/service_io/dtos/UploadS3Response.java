package com.challenge.fastfood.service_io.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadS3Response {

	private String url;
	private String key;
	
}

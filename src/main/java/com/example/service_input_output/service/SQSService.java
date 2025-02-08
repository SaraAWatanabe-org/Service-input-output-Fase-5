package com.example.service_input_output.service;

import com.example.service_input_output.entities.VideoRequestEntity;

public interface SQSService {

	public void sendMessage(VideoRequestEntity videoRequest);

}

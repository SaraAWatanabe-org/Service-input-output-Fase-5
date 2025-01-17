package com.challenge.fastfood.service_io.services;

import com.challenge.fastfood.service_io.entities.VideoRequestEntity;

public interface SQSService {

	public void sendMessage(VideoRequestEntity videoRequest);

}

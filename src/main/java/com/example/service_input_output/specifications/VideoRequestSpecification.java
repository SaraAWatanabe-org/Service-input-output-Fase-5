package com.example.service_input_output.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.example.service_input_output.entities.VideoRequestEntity;

public class VideoRequestSpecification {
	
	public static Specification<VideoRequestEntity> byEmail(String email) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("requester").get("email"), email);
	}

}

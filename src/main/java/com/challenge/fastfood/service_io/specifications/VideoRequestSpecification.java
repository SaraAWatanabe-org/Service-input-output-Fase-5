package com.challenge.fastfood.service_io.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.challenge.fastfood.service_io.entities.VideoRequestEntity;

public class VideoRequestSpecification {
	
	public static Specification<VideoRequestEntity> byEmail(String email) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("requester").get("email"), email);
	}

}

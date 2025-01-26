package com.example.service_input_output.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.service_input_output.entities.VideoRequestEntity;

@Repository
public interface VideoRequestRepository extends JpaRepository<VideoRequestEntity, UUID>, JpaSpecificationExecutor<VideoRequestEntity>{


	Optional<VideoRequestEntity> findByIdAndRequesterEmail(UUID id, String email);

}

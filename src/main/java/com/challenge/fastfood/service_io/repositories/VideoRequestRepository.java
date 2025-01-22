package com.challenge.fastfood.service_io.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.challenge.fastfood.service_io.entities.VideoRequestEntity;

@Repository
public interface VideoRequestRepository extends JpaRepository<VideoRequestEntity, UUID>, JpaSpecificationExecutor<VideoRequestEntity>{


	Optional<VideoRequestEntity> findByIdAndUserEmail(UUID id, String email);

}

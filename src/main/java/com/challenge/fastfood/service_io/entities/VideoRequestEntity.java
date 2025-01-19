package com.challenge.fastfood.service_io.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import com.challenge.fastfood.service_io.enums.RequestStatusEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Data;

@Entity
@Data
public class VideoRequestEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(nullable = false)
	private String status;

	@Column(nullable = false)
	private String url;
	
	@Column(nullable = false)
	private String objectKey;
	
	@Column(nullable = false)
	private LocalDateTime createDate;

	@Column(nullable = false)
	private LocalDateTime lastUpdate;

	@ManyToOne
	@JoinColumn(name = "requester_id")
	private UserEntity requester;

	public RequestStatusEnum getStatus() {
		return RequestStatusEnum.toEnum(status);
	}

	public void setStatus(RequestStatusEnum status) {
		this.status = status.getCode();
	}

	@PrePersist
	public void prePersist() {
		createDate = LocalDateTime.now();
		lastUpdate = LocalDateTime.now();
	}

	@PreUpdate
	public void preUpdate() {
		lastUpdate = LocalDateTime.now();
	}
	
}

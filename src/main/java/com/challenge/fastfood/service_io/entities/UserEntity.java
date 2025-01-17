package com.challenge.fastfood.service_io.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class UserEntity {

	@Id
	@Column(nullable = false)
	private String username;

	@Column(nullable = false)
	private String name;

	private String email;

	private String cpf;

	@OneToMany(mappedBy = "requester")
	private List<VideoRequestEntity> videoRequests = new ArrayList<VideoRequestEntity>();

}

package com.example.service_input_output.entities;

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
	private String email;
	
	@Column(nullable = false)
	private String name;

	private String cpf;
	
	@Column(nullable = false)
	private Boolean isTermAccepted;
	
	@OneToMany(mappedBy = "requester")
	private List<VideoRequestEntity> videoRequests = new ArrayList<VideoRequestEntity>();

}

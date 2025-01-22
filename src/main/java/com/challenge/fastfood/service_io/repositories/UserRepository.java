package com.challenge.fastfood.service_io.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.challenge.fastfood.service_io.entities.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

}

package com.example.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.EnqStatusEntity;

public interface EnqStatusRepo extends JpaRepository<EnqStatusEntity, Integer> {

	
	
}

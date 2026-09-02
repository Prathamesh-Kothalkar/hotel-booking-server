package dev.prathamesh.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.prathamesh.model.RefundModel;

public interface RefundRepo extends JpaRepository<RefundModel, Long>{
	
}
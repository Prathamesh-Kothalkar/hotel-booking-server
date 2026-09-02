package dev.prathamesh.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.prathamesh.model.RoomModel;

public interface RoomRepo extends JpaRepository<RoomModel, Long>{
	
}
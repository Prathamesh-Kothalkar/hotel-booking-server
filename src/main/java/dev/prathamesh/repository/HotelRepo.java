package dev.prathamesh.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.prathamesh.model.HotelModel;

public interface HotelRepo extends JpaRepository<HotelModel, Long>{
	
}
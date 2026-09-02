package dev.prathamesh.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.prathamesh.model.BookingModel;

public interface BookingRepo extends JpaRepository<BookingModel, Long>{
	
}
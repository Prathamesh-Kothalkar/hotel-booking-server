package dev.prathamesh.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.prathamesh.model.PaymentModel;

public interface PaymentRepo extends JpaRepository<PaymentModel, Long>{
	
}
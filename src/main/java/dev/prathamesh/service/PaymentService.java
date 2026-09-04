package dev.prathamesh.service;

import org.springframework.stereotype.Service;

import dev.prathamesh.model.PaymentModel;
import dev.prathamesh.repository.PaymentRepo;

@Service
public class PaymentService{
	private final PaymentRepo paymentRepo;
	
	public PaymentService(PaymentRepo repo) {
		this.paymentRepo=repo;
	}
	
	public PaymentModel getPaymentDetailsById(Long id) {
		return paymentRepo.findById(id).orElse(null);
	}
}
package dev.prathamesh.service;

import org.springframework.stereotype.Service;

import dev.prathamesh.expection.ResourceNotFoundException;
import dev.prathamesh.model.PaymentModel;
import dev.prathamesh.repository.PaymentRepo;

@Service
public class PaymentService{
	private final PaymentRepo paymentRepo;
	
	public PaymentService(PaymentRepo repo) {
		this.paymentRepo=repo;
	}
	
	public PaymentModel getPaymentDetailsById(Long id,Long userId) {
		PaymentModel payment=paymentRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Payment Not found with id :- "+id));
		if(!payment.getUser().getUserId().equals(userId)) {
			throw new IllegalArgumentException("You can't access the payments details");
		}
		return payment;
	}
}
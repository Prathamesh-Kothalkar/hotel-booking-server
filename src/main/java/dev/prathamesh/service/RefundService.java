package dev.prathamesh.service;

import org.springframework.stereotype.Service;

import dev.prathamesh.model.RefundModel;
import dev.prathamesh.repository.RefundRepo;

@Service
public class RefundService{
	private final RefundRepo refundRepo;

	public RefundService(RefundRepo refundRepo) {
		this.refundRepo = refundRepo;
	}
	
	public RefundModel getRefundById(Long id) {
		return refundRepo.findById(id).orElse(null);
	}
	
}
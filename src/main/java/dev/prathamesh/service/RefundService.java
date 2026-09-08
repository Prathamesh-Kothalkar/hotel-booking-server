package dev.prathamesh.service;

import org.springframework.stereotype.Service;

import dev.prathamesh.expection.ResourceNotFoundException;
import dev.prathamesh.model.RefundModel;
import dev.prathamesh.repository.RefundRepo;

@Service
public class RefundService{
	private final RefundRepo refundRepo;

	public RefundService(RefundRepo refundRepo) {
		this.refundRepo = refundRepo;
	}
	
	public RefundModel getRefundById(Long id,Long userID) {
		RefundModel refund=refundRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Refund not Found with id "+id));
		if(!refund.getUser().getUserId().equals(userID)) {
			throw new IllegalArgumentException("You can't access these details");
		}
		
		return refund;
	}
	
}
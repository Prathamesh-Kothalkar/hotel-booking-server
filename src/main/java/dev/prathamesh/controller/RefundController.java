package dev.prathamesh.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.prathamesh.model.RefundModel;
import dev.prathamesh.service.RefundService;

@RestController
@RequestMapping("/api/v1/refunds")
public class RefundController{
	
	@Autowired
	RefundService refundService;
	
	@GetMapping("/hello")
	public String greet() {
		return "Hello From Refund";
	}
	
	@GetMapping("/{id}")
	public RefundModel getRefundById(@PathVariable Long id) {
		return refundService.getRefundById(id);
	}
}
package dev.prathamesh.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.prathamesh.model.PaymentModel;
import dev.prathamesh.service.PaymentService;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController{
	@Autowired
	PaymentService paymentService;
	@GetMapping("/hello")
	public String greet() {
		return "Hello From Payments";
	}
	
	@GetMapping("/{id}")
	public PaymentModel getPaymentDetails(@PathVariable Long id) {
		return paymentService.getPaymentDetailsById(id);
	}
}
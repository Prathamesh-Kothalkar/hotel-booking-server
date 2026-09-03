package dev.prathamesh.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/refunds")
public class RefundController{
	@GetMapping("/hello")
	public String greet() {
		return "Hello From Refund";
	}
}
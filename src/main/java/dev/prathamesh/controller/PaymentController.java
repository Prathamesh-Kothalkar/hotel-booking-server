package dev.prathamesh.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.prathamesh.model.PaymentModel;
import dev.prathamesh.service.PaymentService;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/hello")
    public ResponseEntity<String> greet() {

        return ResponseEntity.ok("Hello From Payments");
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentModel> getPaymentDetails(
            @PathVariable Long id) {

        PaymentModel payment = paymentService.getPaymentDetailsById(id);

        return ResponseEntity.ok(payment);
    }
}
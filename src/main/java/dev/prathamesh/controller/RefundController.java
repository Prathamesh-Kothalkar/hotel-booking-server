package dev.prathamesh.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.prathamesh.model.RefundModel;
import dev.prathamesh.service.RefundService;

@RestController
@RequestMapping("/api/v1/refunds")
public class RefundController {

    private final RefundService refundService;

    public RefundController(RefundService refundService) {
        this.refundService = refundService;
    }

    @GetMapping("/hello")
    public ResponseEntity<String> greet() {

        return ResponseEntity.ok("Hello From Refund");
    }

    @GetMapping("/{id}")
    public ResponseEntity<RefundModel> getRefundById(
            @PathVariable Long id) {

        RefundModel refund = refundService.getRefundById(id);

        return ResponseEntity.ok(refund);
    }
}
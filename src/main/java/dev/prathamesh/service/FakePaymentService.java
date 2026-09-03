package dev.prathamesh.service;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;

@Service
public class FakePaymentService {

    // Simulates calling out to Stripe/Razorpay/etc: adds latency, can fail.
    public PaymentResult charge(Long userId, BigDecimal amount) {
        try {
            Thread.sleep(150); // pretend network round trip to a real gateway
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        boolean success = ThreadLocalRandom.current().nextInt(100) < 95; // 95% success rate
        String transactionId = "TXN-" + UUID.randomUUID();
        return new PaymentResult(success, transactionId);
    }

    public record PaymentResult(boolean success, String transactionId) {}
}
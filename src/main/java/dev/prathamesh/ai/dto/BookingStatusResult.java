package dev.prathamesh.ai.dto;
public record BookingStatusResult(
            Long bookingId,
            String status,
            String checkInDate,
            String checkOutDate,
            java.math.BigDecimal totalAmount
 ) {}
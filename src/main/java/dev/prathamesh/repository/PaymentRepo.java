package dev.prathamesh.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.prathamesh.model.PaymentModel;
import dev.prathamesh.types.PaymentStatus;

public interface PaymentRepo extends JpaRepository<PaymentModel, Long>{
	Optional<PaymentModel> findTopByBooking_BookingIdAndStatusOrderByCreatedAtDesc(
            Long bookingId,
            PaymentStatus status
    );
}
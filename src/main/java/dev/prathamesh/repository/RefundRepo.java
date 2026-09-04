package dev.prathamesh.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.prathamesh.model.PaymentModel;
import dev.prathamesh.model.RefundModel;
import dev.prathamesh.types.RefundStatus;

public interface RefundRepo extends JpaRepository<RefundModel, Long>{
	boolean existsByPaymentAndStatusIn(PaymentModel payment, List<RefundStatus> statuses);
}
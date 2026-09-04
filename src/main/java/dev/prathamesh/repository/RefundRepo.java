package dev.prathamesh.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.prathamesh.model.PaymentModel;
import dev.prathamesh.model.RefundModel;
import dev.prathamesh.types.RefundStatus;

public interface RefundRepo extends JpaRepository<RefundModel, Long>{
	boolean existsByPaymentAndStatusIn(PaymentModel payment, List<RefundStatus> statuses);
	
	@Query("SELECT r FROM RefundModel r WHERE r.user.userId = :userId")
	 List<RefundModel> findByUserId(@Param("userId") Long userId);
}
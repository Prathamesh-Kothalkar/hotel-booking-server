package dev.prathamesh.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import org.hibernate.annotations.CreationTimestamp;


import dev.prathamesh.types.RefundStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(
    name = "refunds",
    indexes = {
        @Index(name = "idx_refunds_booking_id", columnList = "booking_id"),
        @Index(name = "idx_refunds_user_id", columnList = "user_id")
    }
)
public class RefundModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refund_id")
    private Long refundId;

    @ManyToOne
    @JoinColumn(name = "payment_id", nullable = false)
    private PaymentModel payment;

    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private BookingModel booking;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel user;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(length = 255)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RefundStatus status = RefundStatus.REQUESTED;

    @CreationTimestamp
    @Column(name = "requested_at", nullable = false, updatable = false)
    private OffsetDateTime requestedAt;

    @Column(name = "processed_at")
    private OffsetDateTime processedAt;

    public RefundModel() {
    }

	public Long getRefundId() {
		return refundId;
	}

	public void setRefundId(Long refundId) {
		this.refundId = refundId;
	}

	public PaymentModel getPayment() {
		return payment;
	}

	public void setPayment(PaymentModel payment) {
		this.payment = payment;
	}

	public BookingModel getBooking() {
		return booking;
	}

	public void setBooking(BookingModel booking) {
		this.booking = booking;
	}

	public UserModel getUser() {
		return user;
	}

	public void setUser(UserModel user) {
		this.user = user;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public RefundStatus getStatus() {
		return status;
	}

	public void setStatus(RefundStatus status) {
		this.status = status;
	}

	public OffsetDateTime getRequestedAt() {
		return requestedAt;
	}

	public void setRequestedAt(OffsetDateTime requestedAt) {
		this.requestedAt = requestedAt;
	}

	public OffsetDateTime getProcessedAt() {
		return processedAt;
	}

	public void setProcessedAt(OffsetDateTime processedAt) {
		this.processedAt = processedAt;
	}
    
}
package dev.prathamesh.types;

import java.math.BigDecimal;

public class RefundRequest{
	private Long userId;
	private Long paymentId;
	private Long bookingId;
	private BigDecimal amount;
	private String reason;
	private RefundStatus status;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(Long paymentId) {
		this.paymentId = paymentId;
	}
	public Long getBookingId() {
		return bookingId;
	}
	public void setBookingId(Long bookingId) {
		this.bookingId = bookingId;
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
	
	
	
}
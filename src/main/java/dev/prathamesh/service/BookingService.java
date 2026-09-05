package dev.prathamesh.service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.prathamesh.expection.*;
import dev.prathamesh.model.BookingModel;
import dev.prathamesh.model.PaymentModel;
import dev.prathamesh.model.RefundModel;
import dev.prathamesh.model.RoomModel;
import dev.prathamesh.model.UserModel;
import dev.prathamesh.repository.BookingRepo;
import dev.prathamesh.repository.PaymentRepo;
import dev.prathamesh.repository.RefundRepo;
import dev.prathamesh.repository.RoomRepo;
import dev.prathamesh.repository.UserRepo;
import dev.prathamesh.types.BookingRequest;
import dev.prathamesh.types.BookingStatus;
import dev.prathamesh.types.PaymentStatus;
import dev.prathamesh.types.RefundStatus;
import dev.prathamesh.types.RoomStatus;

@Service
public class BookingService {

    private final BookingRepo bookingRepo;
    private final UserRepo userRepo;
    private final RoomRepo roomRepo;
    private final PaymentRepo paymentRepo;
    private final RefundRepo refundRepo;
    private final FakePaymentService paymentService;

    public BookingService(BookingRepo bookingRepo, UserRepo userRepo, RoomRepo roomRepo,
                           PaymentRepo paymentRepo,RefundRepo refundRepo, FakePaymentService paymentService) {
        this.bookingRepo = bookingRepo;
        this.userRepo = userRepo;
        this.roomRepo = roomRepo;
        this.paymentRepo = paymentRepo;
        this.refundRepo=refundRepo;
        this.paymentService = paymentService;
    }
    
    public BookingModel getBookingById(Long id) {
    	return bookingRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));
    }
    
    @Transactional
    public BookingModel cancelBookingId(Long id) {
    	
        BookingModel booking = bookingRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Booking not found with id: " + id));

        
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new IllegalStateException("Booking is already cancelled");
        }

        // 3. Find successful payment for this booking
        PaymentModel payment = paymentRepo
                .findTopByBooking_BookingIdAndStatusOrderByCreatedAtDesc(id, PaymentStatus.SUCCESS)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Successful payment not found for booking id: " + id));

        // 4. Prevent duplicate refund requests — REJECTED is deliberately excluded
        //    so a user can request again after a rejection
        boolean refundAlreadyExists = refundRepo.existsByPaymentAndStatusIn(
                payment,
                List.of(RefundStatus.REQUESTED, RefundStatus.APPROVED, RefundStatus.COMPLETED)
        );

        if (refundAlreadyExists) {
            throw new IllegalStateException("Refund request already exists for this payment");
        }

        // 5. Create refund request
        RefundModel refund = new RefundModel();
        refund.setPayment(payment);
        refund.setBooking(booking);
        refund.setUser(booking.getUser());
        refund.setAmount(payment.getAmount()); // full refund for now
        refund.setReason("Booking cancelled by user");
        refund.setStatus(RefundStatus.REQUESTED);
        refundRepo.save(refund);

        // 6. Update booking status
        booking.setStatus(BookingStatus.CANCELLED);
        
        // NOTE: payment status intentionally left as SUCCESS — it only becomes
        // REFUNDED once the refund is actually processed (separate flow),
        // and room status is untouched — availability is date-range-derived, not a flag.

        // 7. Save booking — @Version here protects against a concurrent
        //    duplicate cancel/refund on the same booking
        return bookingRepo.save(booking);
    }

    @Transactional
    public BookingModel bookRoom(BookingRequest request) {
    	
        if (request.getCheckOutDate() == null || request.getCheckInDate() == null
                || !request.getCheckOutDate().isAfter(request.getCheckInDate())) {
            throw new IllegalArgumentException("check-out date must be after check-in date");
        }

        UserModel user = userRepo.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.getUserId()));

        // PESSIMISTIC LOCK: this blocks any other transaction trying to book the
        // same room until this one commits or rolls back. This is what actually
        // prevents the double-booking race — the overlap check below is only safe
        // BECAUSE this row is locked first.
        RoomModel room = roomRepo.findByIdForUpdate(request.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found: " + request.getRoomId()));

        if (room.getStatus() == dev.prathamesh.types.RoomStatus.MAINTENANCE) {
            throw new RoomNotAvailableException("Room " + room.getRoomId() + " is under maintenance");
        }

        boolean overlapping = bookingRepo.existsOverlappingBooking(
                room.getRoomId(), request.getCheckInDate(), request.getCheckOutDate());
        if (overlapping) {
            throw new RoomNotAvailableException(
                    "Room " + room.getRoomId() + " is already booked for the requested dates");
        }

        long nights = ChronoUnit.DAYS.between(request.getCheckInDate(), request.getCheckOutDate());
        BigDecimal totalAmount = room.getPrice().multiply(BigDecimal.valueOf(nights));

        BookingModel booking = new BookingModel();
        booking.setUser(user);
        booking.setRoom(room);
        booking.setCheckInDate(request.getCheckInDate());
        booking.setCheckOutDate(request.getCheckOutDate());
        booking.setNumGuests(request.getNumGuests() == null ? 1 : request.getNumGuests());
        booking.setTotalAmount(totalAmount);
        booking.setStatus(BookingStatus.PENDING);
        booking = bookingRepo.save(booking);
        
        

        FakePaymentService.PaymentResult result = paymentService.charge(user.getUserId(), totalAmount);

        PaymentModel payment = new PaymentModel();
        payment.setBooking(booking);
        payment.setUser(user);
        payment.setAmount(totalAmount);
        payment.setPaymentMethod("CARD");
        payment.setTransactionId(result.transactionId());
        payment.setStatus(result.success() ? PaymentStatus.SUCCESS : PaymentStatus.FAILED);
        paymentRepo.save(payment);

        if (!result.success()) {
            booking.setStatus(BookingStatus.CANCELLED);
            bookingRepo.save(booking);
            // room was never marked unavailable, so no cleanup needed there —
            // the failed booking simply won't count toward future overlap checks
            throw new PaymentFailedException("Payment failed for booking " + booking.getBookingId());
        }

        booking.setStatus(BookingStatus.CONFIRMED);
//        room.setStatus(RoomStatus.BOOKED);
//        roomRepo.save(room);
        return bookingRepo.save(booking);
    }
}
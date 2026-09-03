package dev.prathamesh.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.prathamesh.model.BookingModel;

public interface BookingRepo extends JpaRepository<BookingModel, Long>{
	@Query("select count(b) > 0 from BookingModel b " +
	           "where b.room.roomId = :roomId " +
	           "and b.status <> dev.prathamesh.types.BookingStatus.CANCELLED " +
	           "and b.checkInDate < :checkOutDate and b.checkOutDate > :checkInDate")
	    boolean existsOverlappingBooking(@Param("roomId") Long roomId,
	                                      @Param("checkInDate") LocalDate checkInDate,
	                                      @Param("checkOutDate") LocalDate checkOutDate);
}
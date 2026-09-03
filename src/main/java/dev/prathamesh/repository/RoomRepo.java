package dev.prathamesh.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import dev.prathamesh.model.RoomModel;
import jakarta.persistence.LockModeType;

public interface RoomRepo extends JpaRepository<RoomModel, Long> {

    @Query("""
        SELECT r
        FROM RoomModel r
        JOIN r.hotel h
        WHERE (:location IS NULL OR LOWER(h.location) = LOWER(:location))
          AND (:guests IS NULL OR r.noOfBeds >= :guests)
          AND (
              (CAST(:checkIn AS date) IS NULL AND CAST(:checkOut AS date) IS NULL) OR
              NOT EXISTS (
                  SELECT b
                  FROM BookingModel b
                  WHERE b.room = r
                    AND b.status <> dev.prathamesh.types.BookingStatus.CANCELLED
                    AND b.checkInDate < CAST(:checkOut AS date)
                    AND b.checkOutDate > CAST(:checkIn AS date)
              )
          )
    """)
    List<RoomModel> searchAvailableRooms(
            @Param("location") String location,
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut,
            @Param("guests") Short guests
    );
    
    @Query("SELECT r FROM RoomModel r WHERE r.hotel.hotel_id = :hotel_id")
    List<RoomModel> findByHotel_hotel_id(Long hotel_id);
    
    //Lock to handle Concurrency
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from RoomModel r where r.roomId = :roomId")
    Optional<RoomModel> findByIdForUpdate(@Param("roomId") Long roomId);
}
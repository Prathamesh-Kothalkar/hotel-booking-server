package dev.prathamesh.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import dev.prathamesh.expection.RoomNotAvailableException;
import dev.prathamesh.model.HotelModel;
import dev.prathamesh.model.RoomModel;
import dev.prathamesh.model.UserModel;
import dev.prathamesh.repository.HotelRepo;
import dev.prathamesh.repository.RoomRepo;
import dev.prathamesh.repository.UserRepo;
import dev.prathamesh.types.BookingRequest;
import dev.prathamesh.types.RoomStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
class ConcurrentBookingTest {

    @Autowired private BookingService bookingService;
    @Autowired private HotelRepo hotelRepo;
    @Autowired private RoomRepo roomRepo;
    @Autowired private UserRepo userRepo;

    private Long roomId;
    private List<Long> userIds;

    private static final int THREAD_COUNT = 10;

    @BeforeEach
    void setUp() {
        HotelModel hotel = new HotelModel();
        hotel.setName("Test Hotel");
        hotel.setLocation("Pune");
        hotel = hotelRepo.save(hotel);

        RoomModel room = new RoomModel();
        room.setHotel(hotel);
        room.setRoomType("DELUXE");
        room.setNoOfBeds((short) 2);
        room.setPrice(BigDecimal.valueOf(4500));
        room.setStatus(RoomStatus.AVAILABLE);
        room = roomRepo.save(room);
        roomId = room.getRoomId();

        userIds = new java.util.ArrayList<>();
        for (int i = 0; i < THREAD_COUNT; i++) {
            UserModel user = new UserModel();
            user.setName("User " + i);
            user.setEmail("user" + i + "@test.com");
            user = userRepo.save(user);
            userIds.add(user.getUserId());
        }
    }

    @Test
    void onlyOneBookingSucceeds_whenMultipleThreadsBookSameRoomSameDates() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch readyLatch = new CountDownLatch(THREAD_COUNT); // all threads report "ready"
        CountDownLatch startLatch = new CountDownLatch(1);           // fires the starting gun
        CountDownLatch doneLatch = new CountDownLatch(THREAD_COUNT);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger conflictCount = new AtomicInteger(0);
        AtomicInteger unexpectedCount = new AtomicInteger(0);

        LocalDate checkIn = LocalDate.of(2026, 9, 10);
        LocalDate checkOut = LocalDate.of(2026, 9, 12);

        for (int i = 0; i < THREAD_COUNT; i++) {
            final Long userId = userIds.get(i);
            executor.submit(() -> {
                try {
                    readyLatch.countDown();
                    startLatch.await(); // all threads block here until released together

                    BookingRequest request = new BookingRequest();
                    request.setUserId(userId);
                    request.setRoomId(roomId);
                    request.setCheckInDate(checkIn);
                    request.setCheckOutDate(checkOut);
                    request.setNumGuests((short) 2);

                    bookingService.bookRoom(request);
                    successCount.incrementAndGet();

                } catch (RoomNotAvailableException e) {
                    conflictCount.incrementAndGet();
                } catch (Exception e) {
                    unexpectedCount.incrementAndGet();
                    e.printStackTrace(); // surface anything you didn't expect
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        readyLatch.await();               // wait for all threads to line up
        startLatch.countDown();            // release them all at once — maximum contention
        doneLatch.await(30, TimeUnit.SECONDS);
        executor.shutdown();

        System.out.printf("Success: %d, Conflict: %d, Unexpected: %d%n",
                successCount.get(), conflictCount.get(), unexpectedCount.get());

        assertEquals(1, successCount.get(), "Exactly one booking should succeed for the same room/dates");
        assertEquals(THREAD_COUNT - 1, conflictCount.get(), "All others should be rejected as room unavailable");
        assertEquals(0, unexpectedCount.get(), "No unexpected exceptions should occur");
    }
}
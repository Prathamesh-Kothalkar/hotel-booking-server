package dev.prathamesh.types;

import java.time.LocalDate;

public class BookingRequest {

    private Long userId;
    private Long roomId;

    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    private Short numGuests;

    public BookingRequest() {
    	
    }
    
    

    public BookingRequest(Long userId, Long roomId, LocalDate checkInDate, LocalDate checkOutDate, Short numGuests) {
		this.userId = userId;
		this.roomId = roomId;
		this.checkInDate = checkInDate;
		this.checkOutDate = checkOutDate;
		this.numGuests = numGuests;
	}



	public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public Short getNumGuests() {
        return numGuests;
    }

    public void setNumGuests(Short numGuests) {
        this.numGuests = numGuests;
    }
}
package dev.prathamesh.types;

import java.math.BigDecimal;

public class RoomSearchResponse {

    private Long roomId;

    private String roomType;

    private Short noOfBeds;

    private BigDecimal price;

    private RoomStatus status;

    private String hotelName;

    private String location;

    private BigDecimal rating;

    private String[] images;

	public Long getRoomId() {
		return roomId;
	}

	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}

	public String getRoomType() {
		return roomType;
	}

	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}

	public Short getNoOfBeds() {
		return noOfBeds;
	}

	public void setNoOfBeds(Short noOfBeds) {
		this.noOfBeds = noOfBeds;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public RoomStatus getStatus() {
		return status;
	}

	public void setStatus(RoomStatus status) {
		this.status = status;
	}

	public String getHotelName() {
		return hotelName;
	}

	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public BigDecimal getRating() {
		return rating;
	}

	public void setRating(BigDecimal rating) {
		this.rating = rating;
	}

	public String[] getImages() {
		return images;
	}

	public void setImages(String[] images) {
		this.images = images;
	}

    // getters/setters
    
    
}
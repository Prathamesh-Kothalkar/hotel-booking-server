package dev.prathamesh.types;

import java.math.BigDecimal;

public class CreateRoomRequest {

    private Long hotelId;
    private String roomType;
    private Short noOfBeds;
    private BigDecimal price;
    private RoomStatus status;
    private String[] images;
    
	public Long getHotelId() {
		return hotelId;
	}
	public void setHotelId(Long hotelId) {
		this.hotelId = hotelId;
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
	public String[] getImages() {
		return images;
	}
	public void setImages(String[] images) {
		this.images = images;
	}

    // getters + setters
    
}
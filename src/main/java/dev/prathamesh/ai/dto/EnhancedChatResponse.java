package dev.prathamesh.ai.dto;

import java.math.BigDecimal;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EnhancedChatResponse {
    
    private String reply;  // Human-readable text response from LLM
    
    @JsonProperty("responseType")
    private String responseType;  // SEARCH_RESULTS, BOOKING_CONFIRMATION, QUESTION, etc.
    
    @JsonProperty("searchResults")
    private List<RoomResult> searchResults;
    
    @JsonProperty("bookingConfirmation")
    private BookingConfirmation bookingConfirmation;
    
    @JsonProperty("suggestedActions")
    private List<String> suggestedActions;
    
    @JsonProperty("error")
    private String error;

    // Constructors
    public EnhancedChatResponse() {}

    public EnhancedChatResponse(String reply) {
        this.reply = reply;
        this.responseType = "TEXT";
    }

    public EnhancedChatResponse(String reply, String responseType) {
        this.reply = reply;
        this.responseType = responseType;
    }

    // Getters and setters
    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getResponseType() {
        return responseType;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }

    public List<RoomResult> getSearchResults() {
        return searchResults;
    }

    public void setSearchResults(List<RoomResult> searchResults) {
        this.searchResults = searchResults;
    }

    public BookingConfirmation getBookingConfirmation() {
        return bookingConfirmation;
    }

    public void setBookingConfirmation(BookingConfirmation bookingConfirmation) {
        this.bookingConfirmation = bookingConfirmation;
    }

    public List<String> getSuggestedActions() {
        return suggestedActions;
    }

    public void setSuggestedActions(List<String> suggestedActions) {
        this.suggestedActions = suggestedActions;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    // ============= INNER CLASSES =============

    /**
     * Structured room result for search responses
     */
    public static class RoomResult {
        private Long roomId;
        private String hotelName;
        private String location;
        private String roomType;
        private Integer noOfBeds;
        private Double price;
        private BigDecimal rating;
        private String status;
        private String[] images;
        private String checkIn;
        private String checkOut;

        public RoomResult() {}

        public RoomResult(Long roomId, String hotelName, String location, 
                         String roomType, Integer noOfBeds, Double price) {
            this.roomId = roomId;
            this.hotelName = hotelName;
            this.location = location;
            this.roomType = roomType;
            this.noOfBeds = noOfBeds;
            this.price = price;
        }

        // Getters and setters
        public Long getRoomId() { return roomId; }
        public void setRoomId(Long roomId) { this.roomId = roomId; }

        public String getHotelName() { return hotelName; }
        public void setHotelName(String hotelName) { this.hotelName = hotelName; }

        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }

        public String getRoomType() { return roomType; }
        public void setRoomType(String roomType) { this.roomType = roomType; }

        public Integer getNoOfBeds() { return noOfBeds; }
        public void setNoOfBeds(int i) { this.noOfBeds = i; }

        public Double getPrice() { return price; }
        public void setPrice(Double price) { this.price = price; }

        public BigDecimal getRating() { return rating; }
        public void setRating(BigDecimal bigDecimal) { this.rating = bigDecimal; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public String[] getImages() { return images; }
        public void setImages(String[] strings) { this.images = strings; }

        public String getCheckIn() { return checkIn; }
        public void setCheckIn(String checkIn) { this.checkIn = checkIn; }

        public String getCheckOut() { return checkOut; }
        public void setCheckOut(String checkOut) { this.checkOut = checkOut; }
    }

    /**
     * Structured booking confirmation
     */
    public static class BookingConfirmation {
        private Long bookingId;
        private String hotel;
        private String roomType;
        private Integer roomId;
        private String checkInDate;
        private String checkOutDate;
        private Integer numGuests;
        private Double totalAmount;
        private String status;  // CONFIRMED, PENDING, etc.
        private String bookingDate;

        public BookingConfirmation() {}

        public BookingConfirmation(Long bookingId, String hotel, String checkInDate, 
                                   String checkOutDate, Double totalAmount) {
            this.bookingId = bookingId;
            this.hotel = hotel;
            this.checkInDate = checkInDate;
            this.checkOutDate = checkOutDate;
            this.totalAmount = totalAmount;
            this.status = "CONFIRMED";
        }

        // Getters and setters
        public Long getBookingId() { return bookingId; }
        public void setBookingId(Long bookingId) { this.bookingId = bookingId; }

        public String getHotel() { return hotel; }
        public void setHotel(String hotel) { this.hotel = hotel; }

        public String getRoomType() { return roomType; }
        public void setRoomType(String roomType) { this.roomType = roomType; }

        public Integer getRoomId() { return roomId; }
        public void setRoomId(Integer roomId) { this.roomId = roomId; }

        public String getCheckInDate() { return checkInDate; }
        public void setCheckInDate(String checkInDate) { this.checkInDate = checkInDate; }

        public String getCheckOutDate() { return checkOutDate; }
        public void setCheckOutDate(String checkOutDate) { this.checkOutDate = checkOutDate; }

        public Integer getNumGuests() { return numGuests; }
        public void setNumGuests(Integer numGuests) { this.numGuests = numGuests; }

        public Double getTotalAmount() { return totalAmount; }
        public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public String getBookingDate() { return bookingDate; }
        public void setBookingDate(String bookingDate) { this.bookingDate = bookingDate; }
    }
}
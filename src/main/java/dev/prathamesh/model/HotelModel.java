package dev.prathamesh.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name = "HotelModel")
@Table(name="hotels")
public class HotelModel{
	 	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "hotel_id")
	    private Long hotel_id;

	    @Column(nullable = false, length = 150)
	    private String name;

	    @Column(nullable = false, length = 150)
	    private String location;

	    @Column(columnDefinition = "TEXT")
	    private String description;

	    @Column(precision = 2, scale = 1)
	    private BigDecimal rating;

	    @Column(columnDefinition = "TEXT[]")
	    private String[] images;

	    @Column(name = "created_at", nullable = false, updatable = false)
	    private OffsetDateTime createdAt;

	    @Column(name = "updated_at", nullable = false)
	    private OffsetDateTime updatedAt;
	    
	    public HotelModel() {
	    	
	    }
	
	
	public Long getId() {
		return hotel_id;
	}

	public void setId(Long id) {
		this.hotel_id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDesc() {
		return description;
	}

	public void setDesc(String desc) {
		this.description = desc;
	}

	public BigDecimal getRating() {
		return rating;
	}

	public void setRating(BigDecimal rating) {
		this.rating = rating;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String[] getImages() {
		return images;
	}

	public void setImages(String[] images) {
		this.images = images;
	}

	public OffsetDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(OffsetDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public OffsetDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(OffsetDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}	
	
}
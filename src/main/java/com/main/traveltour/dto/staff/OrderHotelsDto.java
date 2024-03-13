package com.main.traveltour.dto.staff;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.main.traveltour.entity.OrderHotelDetails;
import com.main.traveltour.entity.TourDetails;
import com.main.traveltour.entity.Users;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * DTO for {@link com.main.traveltour.entity.OrderHotels}
 */
@Data
public class OrderHotelsDto {
    String id;
    Integer userId;
    String customerName;
    String customerCitizenCard;
    String customerPhone;
    String customerEmail;
    Integer capacityAdult;
    Integer capacityKid;
    Timestamp checkIn;
    Timestamp checkOut;
    BigDecimal orderTotal;
    String paymentMethod;
    String orderCode;
    Timestamp dateCreated;
    Integer orderStatus;
    String orderNote;
}
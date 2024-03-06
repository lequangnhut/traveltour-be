package com.main.traveltour.dto.staff;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.main.traveltour.entity.*;
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
 * DTO for {@link com.main.traveltour.entity.BookingTours}
 */
@Data
public class BookingToursDto {
    String id;
    Integer userId;
    String tourDetailId;
    String customerName;
    String customerCitizenCard;
    String customerPhone;
    String customerEmail;
    Integer capacityAdult;
    Integer capacityKid;
    Integer capacityBaby;
    BigDecimal orderTotal;
    Integer paymentMethod;
    String orderCode;
    Timestamp dateCreated;
    Integer orderStatus;
    String orderNote;
    Collection<BookingTourCustomers> bookingTourCustomersById;
    Users usersByUserId;
    TourDetails tourDetailsByTourDetailId;
    Tours toursByTourId;
}
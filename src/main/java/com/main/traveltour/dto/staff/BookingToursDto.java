package com.main.traveltour.dto.staff;

import com.main.traveltour.entity.*;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;

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

    Timestamp dateCancelled;

    Integer orderStatus;

    String orderNote;

    Collection<BookingTourCustomers> bookingTourCustomersById;

    Collection<Invoices> invoicesById;

    Collection<Contracts> contractsById;

    Users usersByUserId;

    TourDetails tourDetailsByTourDetailId;

    Tours toursByTourId;
}
package com.main.traveltour.dto.customer.booking;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * DTO for {@link com.main.traveltour.entity.BookingTours}
 */
@Data
public class BookingToursDto implements Serializable {

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
}
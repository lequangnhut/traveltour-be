package com.main.traveltour.dto.customer.infomation;



import com.main.traveltour.entity.OrderHotelDetails;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;

/**
 * DTO for {@link com.main.traveltour.entity.OrderHotels}
 */
@Data
public class OrderHotelsDto implements Serializable {
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
    Collection<OrderHotelDetails> orderHotelDetailsById;
}
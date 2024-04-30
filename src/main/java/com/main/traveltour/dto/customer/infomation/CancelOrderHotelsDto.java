package com.main.traveltour.dto.customer.infomation;

import com.main.traveltour.entity.OrderHotelDetails;
import com.main.traveltour.entity.PaymentMethod;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;

/**
 * DTO for {@link com.main.traveltour.entity.OrderHotels}
 */
@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CancelOrderHotelsDto implements Serializable {
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
    String reasonNote;
    Collection<OrderHotelDetails> orderHotelDetails;
    PaymentMethod method;
    int coc;
    BigDecimal moneyBack;
}
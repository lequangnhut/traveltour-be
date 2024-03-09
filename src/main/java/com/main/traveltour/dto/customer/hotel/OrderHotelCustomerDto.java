package com.main.traveltour.dto.customer.hotel;

import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OrderHotelCustomerDto {
    private String id;
    private Integer userId;
    private String customerName;
    private String customerCitizenCard;
    private String customerPhone;
    private String customerEmail;
    private Integer capacityAdult;
    private Integer capacityKid;
    private Timestamp checkIn;
    private Timestamp checkOut;
    private BigDecimal orderTotal;
    private String paymentMethod;
    private String orderCode;
    private Timestamp dateCreated;
    private Integer orderStatus;
    private String orderNote;
}

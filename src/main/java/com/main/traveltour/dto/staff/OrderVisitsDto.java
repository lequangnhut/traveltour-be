package com.main.traveltour.dto.staff;

import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class OrderVisitsDto {
    String id;
    Integer userId;
    String visitLocationId;
    String customerName;
    String customerCitizenCard;
    String customerPhone;
    String customerEmail;
    Integer capacityAdult;
    Integer capacityKid;
    Timestamp checkIn;
    BigDecimal orderTotal;
    Integer paymentMethod;
    String orderCode;
    Timestamp dateCreated;
    Integer orderStatus;
    String orderNote;
}
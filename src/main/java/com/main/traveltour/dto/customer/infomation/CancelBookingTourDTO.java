package com.main.traveltour.dto.customer.infomation;

import com.main.traveltour.entity.TourDetails;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class CancelBookingTourDTO {

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

    String reasonNote;

    int coc;

    BigDecimal moneyBack;
}

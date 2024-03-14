package com.main.traveltour.dto.customer.infomation;

import com.main.traveltour.entity.TourDetails;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class CancelBookingTourCusDTO {

    String id;

    int userId;

    String tourDetailId;

    String customerName;

    String customerCitizenCard;

    String customerPhone;

    String customerEmail;

    int capacityAdult;

    int capacityKid;

    int capacityBaby;

    BigDecimal orderTotal;

    int paymentMethod; // 0: travel | 1: VNPay | 2: ZaLoPay | 3: Momo

    String orderCode;

    Timestamp dateCreated;
    
    TourDetails tourDetails;
}

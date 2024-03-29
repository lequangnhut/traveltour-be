package com.main.traveltour.dto.customer.infomation;

import com.main.traveltour.entity.OrderVisitDetails;
import com.main.traveltour.entity.VisitLocations;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * DTO for {@link com.main.traveltour.entity.OrderVisits}
 */
@Data
public class OrderVisitsDto implements Serializable {
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
    VisitLocations visitLocationsByVisitLocationId;
}
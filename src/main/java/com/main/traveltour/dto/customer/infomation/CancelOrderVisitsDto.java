package com.main.traveltour.dto.customer.infomation;

import com.main.traveltour.entity.OrderVisitDetails;
import com.main.traveltour.entity.VisitLocations;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * DTO for {@link com.main.traveltour.entity.OrderVisits}
 */
@Data
public class CancelOrderVisitsDto implements Serializable {
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
    String reasonNote;
    Timestamp dateCreated;
    Integer orderStatus;
    String orderNote;
    int coc;
    BigDecimal moneyBack;
    VisitLocations visitLocations;
    List<OrderVisitDetails> orderVisitDetailsById;
}
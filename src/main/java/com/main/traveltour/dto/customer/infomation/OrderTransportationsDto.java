package com.main.traveltour.dto.customer.infomation;

import com.main.traveltour.entity.TransportationBrands;
import com.main.traveltour.entity.TransportationSchedules;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * DTO for {@link com.main.traveltour.entity.OrderTransportations}
 */
@Data
public class OrderTransportationsDto implements Serializable {
    String id;
    Integer userId;
    String transportationScheduleId;
    String customerName;
    String customerCitizenCard;
    String customerPhone;
    String customerEmail;
    Integer amountTicket;
    BigDecimal orderTotal;
    Boolean paymentMethod;
    String orderCode;
    Timestamp dateCreated;
    Integer orderStatus;
    String orderNote;
    TransportationSchedules transportationSchedules;
    TransportationBrands transportationBrands;
}
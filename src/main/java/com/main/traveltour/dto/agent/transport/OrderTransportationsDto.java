package com.main.traveltour.dto.agent.transport;

import lombok.Data;

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

    String priceFormat;

    BigDecimal orderTotal;

    Integer paymentMethod;

    String orderCode;

    Timestamp dateCreated;

    Integer orderStatus;

    String orderNote;
}
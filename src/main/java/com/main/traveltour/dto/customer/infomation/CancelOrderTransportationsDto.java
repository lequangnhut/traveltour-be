package com.main.traveltour.dto.customer.infomation;

import com.main.traveltour.entity.OrderTransportationDetails;
import com.main.traveltour.entity.TransportationBrands;
import com.main.traveltour.entity.TransportationSchedules;
import lombok.Data;
import lombok.ToString;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;

/**
 * DTO for {@link com.main.traveltour.entity.OrderTransportations}
 */
@Data
@ToString
public class CancelOrderTransportationsDto implements Serializable {
    String id;
    Integer userId;
    String transportationScheduleId;
    String customerName;
    String customerCitizenCard;
    String customerPhone;
    String customerEmail;
    Integer amountTicket;
    BigDecimal orderTotal;
    Integer paymentMethod;
    String orderCode;
    Timestamp dateCreated;
    Integer orderStatus;
    String orderNote;
    String reasonNote;
    int coc;
    BigDecimal moneyBack;
    Collection<OrderTransportationDetails> orderTransportationDetails;
    TransportationSchedules transportationSchedulesByTransportationScheduleId;
    TransportationBrands transportationBrands;
}
package com.main.traveltour.dto.customer.infomation;

import com.main.traveltour.entity.VisitLocationTickets;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.main.traveltour.entity.OrderVisitDetails}
 */
@Data
public class OrderVisitDetailsDto implements Serializable {
    int id;
    String orderVisitId;
    Integer visitLocationTicketId;
    Integer amount;
    BigDecimal unitPrice;
    VisitLocationTickets visitLocationTickets;
}
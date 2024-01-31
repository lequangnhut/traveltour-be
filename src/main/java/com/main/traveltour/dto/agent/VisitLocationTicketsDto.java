package com.main.traveltour.dto.agent;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.main.traveltour.entity.VisitLocationTickets}
 */
@Data
public class VisitLocationTicketsDto implements Serializable {

    int id;

    String visitLocationId;

    String ticketTypeName;

    BigDecimal unitPrice;
}
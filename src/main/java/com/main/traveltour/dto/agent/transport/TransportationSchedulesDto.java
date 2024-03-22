package com.main.traveltour.dto.agent.transport;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * DTO for {@link com.main.traveltour.entity.TransportationSchedules}
 */
@Data
public class TransportationSchedulesDto implements Serializable {

    String id;

    String transportationId;

    String fromLocation;

    String toLocation;

    String fromAddress;

    String toAddress;

    Timestamp departureTime;

    Timestamp arrivalTime;

    BigDecimal unitPrice;

    String priceFormat;

    Timestamp dateCreated;

    Timestamp dateDeleted;

    Boolean isActive;

    Integer bookedSeat;

    Boolean tripType;

    Integer isStatus;

    String scheduleNote;
}
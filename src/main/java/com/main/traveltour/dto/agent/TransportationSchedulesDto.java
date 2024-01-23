package com.main.traveltour.dto.agent;

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

    Timestamp departureTime;

    Timestamp arrivalTime;

    BigDecimal unitPrice;

    Timestamp dateCreated;

    Timestamp dateDeleted;

    Boolean isActive;

    Integer bookedSeat;

    Boolean tripType;

    Integer isStatus;
}
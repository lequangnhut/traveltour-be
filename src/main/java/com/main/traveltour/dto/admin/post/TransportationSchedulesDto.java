package com.main.traveltour.dto.admin.post;

import com.main.traveltour.entity.TransportationBrands;
import com.main.traveltour.entity.Transportations;
import lombok.Data;
import lombok.Value;

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

    String fromAddress;

    String toLocation;

    String toAddress;

    Timestamp departureTime;

    Timestamp arrivalTime;

    BigDecimal unitPrice;

    Timestamp dateCreated;

    Timestamp dateDeleted;

    Boolean isActive;

    Integer bookedSeat;

    Boolean tripType;

    Integer isStatus;

    String scheduleNote;

    Transportations transportations;

    TransportationBrands transportationsByTransportationId;
}
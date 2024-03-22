package com.main.traveltour.dto.customer.transport;

import com.main.traveltour.entity.TransportationBrands;
import com.main.traveltour.entity.TransportationScheduleSeats;
import com.main.traveltour.entity.TransportationTypes;
import com.main.traveltour.entity.Transportations;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;

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

    Timestamp dateCreated;

    Timestamp dateDeleted;

    Boolean isActive;

    Integer bookedSeat;

    Boolean tripType;

    Integer isStatus;

    String scheduleNote;

    Transportations transportations;

    TransportationTypes transportationTypes;

    TransportationBrands transportationBrands;

    Collection<TransportationScheduleSeats> transportationScheduleSeatsById;
}
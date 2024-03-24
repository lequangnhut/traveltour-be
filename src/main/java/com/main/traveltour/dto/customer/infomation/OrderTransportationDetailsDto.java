package com.main.traveltour.dto.customer.infomation;

import com.main.traveltour.entity.TransportationScheduleSeats;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.main.traveltour.entity.OrderTransportationDetails}
 */
@Data
public class OrderTransportationDetailsDto implements Serializable {
    Integer id;
    String OrderTransportationId;
    Integer TransportationScheduleSeatId;
    TransportationScheduleSeats transportationScheduleSeats;
}
package com.main.traveltour.dto.agent.transport;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * DTO for {@link com.main.traveltour.entity.Transportations}
 */
@Data
public class TransportationsDto implements Serializable {

    String id;

    String transportationBrandId;

    Integer transportationTypeId;

    String licensePlate;

    Integer amountSeat;

    Timestamp dateCreated;

    Boolean isActive;

    Boolean isTransportBed;

    Integer columnSeat;
}
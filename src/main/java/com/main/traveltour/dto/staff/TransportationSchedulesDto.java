package com.main.traveltour.dto.staff;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.main.traveltour.entity.OrderTransportations;
import com.main.traveltour.entity.TransportationBrands;
import com.main.traveltour.entity.TransportationTypes;
import com.main.traveltour.entity.Transportations;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;

@Data
public class TransportationSchedulesDto {

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

    Collection<OrderTransportations> orderTransportationsById;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    Transportations transportationsByTransportationId;

    @JsonIgnoreProperties("transportationsById")
    TransportationTypes transportationTypesByTransportationTypeId;

    @JsonIgnoreProperties("transportationsById")
    TransportationBrands transportationBrandsByTransportationBrandId;
}
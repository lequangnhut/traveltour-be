package com.main.traveltour.dto.staff.requestcar;

import com.main.traveltour.entity.RequestCar;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * DTO for {@link RequestCar}
 */
@Data
public class RequestCarDto implements Serializable {

    Integer id;

    String tourDetailId;

    Integer amountCustomer;

    Timestamp departureDate;

    Timestamp arrivalDate;

    String fromLocation;

    String toLocation;

    Boolean isTransportBed;

    Timestamp dateCreated;

    Boolean isAccepted;

    Boolean isActive;

    String requestCarNoted;
}
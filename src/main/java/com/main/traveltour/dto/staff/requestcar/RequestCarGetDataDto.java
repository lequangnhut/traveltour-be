package com.main.traveltour.dto.staff.requestcar;

import com.main.traveltour.entity.*;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;

/**
 * DTO for {@link RequestCar}
 */
@Data
public class RequestCarGetDataDto implements Serializable {

    Integer id;

    String tourDetailId;

    Integer amountCustomer;

    Timestamp departureDate;

    Timestamp arrivalDate;

    String fromLocation;

    String toLocation;

    Boolean isTransportBed;

    Timestamp dateCreated;

    Timestamp dateAccepted;

    Boolean isAccepted;

    Boolean isActive;

    String requestCarNoted;

    Tours toursByTourId;

    TourTypes tourTypesById;

    Collection<RequestCarDetail> requestCarDetailsById;
}
package com.main.traveltour.dto.staff.tour;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * DTO for {@link com.main.traveltour.entity.TourDetails}
 */
@Data
public class TourDetailsDto {

    String id;

    String tourId;

    Integer guideId;

    Timestamp departureDate;

    Timestamp arrivalDate;

    Integer numberOfGuests;

    Integer minimumNumberOfGuests;

    BigDecimal unitPrice;

    String tourDetailNotes;

    Integer tourDetailStatus;

    Timestamp dateCreated;

    String tourDetailDescription;

    Integer bookedSeat;

    String fromLocation;

    String toLocation;
}
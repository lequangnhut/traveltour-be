package com.main.traveltour.dto.staff;

import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * DTO for {@link com.main.traveltour.entity.TourDetails}
 */
@Data
public class TourDetailsDto {
    int id;
    Integer tourId;
    Integer guideId;
    Date departureDate;
    Date arrivalDate;
    Integer numberOfGuests;
    BigDecimal unitPrice;
    String tourDetailNotes;
    Integer tourDetailStatus;
    Timestamp dateCreated;
    String tourDetailDescription;
}
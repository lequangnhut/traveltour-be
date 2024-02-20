package com.main.traveltour.dto.customer;

import com.main.traveltour.entity.TourDetailImages;
import com.main.traveltour.entity.Tours;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;

/**
 * DTO for {@link com.main.traveltour.entity.TourDetails}
 */
@Data
public class TourDetailsDto implements Serializable {

    String id;

    String tourId;

    Integer guideId;

    Date departureDate;

    Date arrivalDate;

    Integer numberOfGuests;

    Integer minimumNumberOfGuests;

    BigDecimal unitPrice;

    String tourDetailNotes;

    Integer tourDetailStatus;

    Timestamp dateCreated;

    Timestamp dateDeleted;

    String tourDetailDescription;

    String fromLocation;

    String toLocation;

    Tours toursByTourId;

    Collection<TourDetailImages> tourDetailImagesById;
}
package com.main.traveltour.dto.guide;

import com.main.traveltour.entity.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

/**
 * DTO for {@link com.main.traveltour.entity.TourDetails}
 */
@Data
public class TourDetailsDtoGuide implements Serializable {
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

    Timestamp dateDeleted;

    String tourDetailDescription;

    Integer bookedSeat;

    String fromLocation;

    String toLocation;

    Collection<BookingTours> bookingToursById;

    Collection<TourDetailImages> tourDetailImagesById;

    Collection<TourDestinations> tourDestinationsById;

    Collection<TourTrips> tourTripsById;

    Tours toursByTourId;

    List<OrderHotels> orderHotels;

    List<OrderTransportations> orderTransportations;

    List<OrderVisits> orderVisits;
}
package com.main.traveltour.dto.staff.tour;

import com.main.traveltour.entity.TourDetailImages;
import com.main.traveltour.entity.TourTypes;
import com.main.traveltour.entity.Tours;
import com.main.traveltour.entity.Users;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class TourDetailsRatingDto {
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
    Tours toursByTourId;
    TourTypes tourTypes;
    Collection<TourDetailImages> tourDetailImagesById;
    Users usersByGuideId;
    Double rate;
    Integer countRating;
}

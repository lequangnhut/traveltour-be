package com.main.traveltour.dto.staff.tour;

import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Time;

/**
 * DTO for {@link com.main.traveltour.entity.TourTrips}
 */
@Data
public class TourTripsDto {

    int id;

    String tourDetailId;

    Integer transportationTypeId;

    String placeName;

    String placeAddress;

    BigDecimal placeCost;

    String activityInDay;

    Integer dayInTrip;
}
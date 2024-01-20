package com.main.traveltour.dto.staff;

import lombok.Data;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.main.traveltour.entity.TourTrips}
 */
@Data
public class TourTripsDto {
    int id;
    Integer tourId;
    Integer dayInTrip;
    String activityInDay;
}
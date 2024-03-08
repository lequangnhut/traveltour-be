package com.main.traveltour.dto.staff.tour;

import com.main.traveltour.entity.TransportationTypes;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Time;

/**
 * DTO for {@link com.main.traveltour.entity.TourTrips}
 */
@Data
public class TourTripGetDataDto {

    int id;

    String tourDetailId;

    Integer transportationTypeId;

    String placeName;

    String placeImage;

    String placeAddress;

    BigDecimal placeCost;

    String activityInDay;

    Integer dayInTrip;

    Time timeGo;

    TransportationTypes transportationTypesByTransportationTypeId;
}

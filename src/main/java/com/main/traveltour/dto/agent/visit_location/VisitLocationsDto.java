package com.main.traveltour.dto.agent.visit_location;

import lombok.Data;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * DTO for {@link com.main.traveltour.entity.VisitLocations}
 */
@Data
public class VisitLocationsDto implements Serializable {

    String id;

    String visitLocationName;

    String urlWebsite;

    String phone;

    String province;

    String district;

    String ward;

    String address;

    Time openingTime;

    Time closingTime;

    Timestamp dateCreated;

    Boolean isAccepted;

    String detailDescription;

    Boolean isActive;

    int visitLocationTypeId;

    int agenciesId;
}
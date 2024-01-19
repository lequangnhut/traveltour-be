package com.main.traveltour.dto.agent;

import lombok.Data;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * DTO for {@link com.main.traveltour.entity.VisitLocations}
 */
@Data
public class VisitLocationsDto implements Serializable {

    int id;

    String visitLocationName;

    String urlWebsite;

    String phone;

    String province;

    String district;

    String ward;

    String address;

    Time openingTime;

    Time closingTime;

    int userId;

    int visitLocationTypeId;

    Timestamp dateCreated;
}
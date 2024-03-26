package com.main.traveltour.dto.staff;

import com.main.traveltour.entity.*;
import lombok.Data;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Collection;

@Data
public class VisitLocationsDto {

    String id;

    String visitLocationName;

    String visitLocationImage;

    String urlWebsite;

    String phone;

    String province;

    String district;

    String ward;

    String address;

    Time openingTime;

    Time closingTime;

    Timestamp dateCreated;

    Timestamp dateDeleted;

    Boolean isAccepted;

    Boolean isActive;

    String detailDescription;

    int visitLocationTypeId;

    int agenciesId;

    Collection<OrderVisits> orderVisitsById;

    Collection<VisitLocationTickets> visitLocationTicketsById;

    Collection<VisitLocationImage> visitLocationImagesById;

    VisitLocationTypes visitLocationTypesByVisitLocationTypeId;

    Agencies agenciesByAgenciesId;
}
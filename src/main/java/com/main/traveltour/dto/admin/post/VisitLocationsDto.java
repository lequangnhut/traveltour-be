package com.main.traveltour.dto.admin.post;

import com.main.traveltour.entity.Agencies;
import com.main.traveltour.entity.VisitLocationTickets;
import com.main.traveltour.entity.VisitLocationTypes;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Collection;

/**
 * DTO for {@link com.main.traveltour.entity.VisitLocations}
 */
@Data
public class VisitLocationsDto implements Serializable {
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
    int visitLocationTypeId;
    int agenciesId;
    Agencies agencies;
    VisitLocationTypes visitLocationTypes;
    Collection<VisitLocationTickets> visitLocationTicketsById;
}
package com.main.traveltour.dto.customer.transport;

import com.main.traveltour.entity.Agencies;
import com.main.traveltour.entity.Transportations;
import lombok.*;

import java.sql.Timestamp;
import java.util.Collection;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransportationBrandsRatingDto {
    String id;
    String transportationBrandName;
    int agenciesId;
    Timestamp dateCreated;
    Timestamp dateDeleted;
    Boolean isAccepted;
    Boolean isActive;
    String transportationBrandImg;
    String transportationBrandPolicy;
    String transportationBrandAddress;
    Collection<Transportations> transportationsById;
    Agencies agenciesByAgenciesId;
    Double rate;
    Integer countRating;
}

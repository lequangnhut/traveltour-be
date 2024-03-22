package com.main.traveltour.dto.customer;

import com.main.traveltour.entity.Agencies;
import com.main.traveltour.entity.Transportations;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;

/**
 * DTO for {@link com.main.traveltour.entity.TransportationBrands}
 */
@Data
public class TransportationBrandsDto implements Serializable {

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
}
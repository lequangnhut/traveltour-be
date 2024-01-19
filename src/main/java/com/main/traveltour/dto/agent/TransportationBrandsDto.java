package com.main.traveltour.dto.agent;

import com.main.traveltour.entity.TransportationBrands;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * DTO for {@link TransportationBrands}
 */
@Data
public class TransportationBrandsDto implements Serializable {

    int id;

    String transportationBrandName;

    int agenciesId;

    int userId;

    Timestamp dateCreated;

    Boolean isAccepted;

    Boolean isActive;

    String transportationBrandDescription;
}
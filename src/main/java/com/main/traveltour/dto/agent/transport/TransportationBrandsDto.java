package com.main.traveltour.dto.agent.transport;

import com.main.traveltour.entity.TransportationBrands;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * DTO for {@link TransportationBrands}
 */
@Data
public class TransportationBrandsDto implements Serializable {

    String id;

    int agenciesId;

    String transportationBrandName;

    Timestamp dateCreated;

    Boolean isAccepted;

    Boolean isActive;

    String transportationBrandPolicy;

    String transportationBrandAddress;
}
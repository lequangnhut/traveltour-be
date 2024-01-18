package com.main.traveltour.dto.agent.transport;

import com.main.traveltour.entity.TransportationBrands;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link TransportationBrands}
 */
@Data
public class TransportationBrandsDto implements Serializable {

    int id;

    String transportationBrandName;

    int agenciesId;

    int userId;

    Boolean isActive;

    String transportationBrandImg;

    String transportationBrandDescription;
}
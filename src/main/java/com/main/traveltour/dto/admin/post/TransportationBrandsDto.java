package com.main.traveltour.dto.admin.post;

import com.main.traveltour.entity.Agencies;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.sql.Timestamp;

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
    Agencies agencies;
}
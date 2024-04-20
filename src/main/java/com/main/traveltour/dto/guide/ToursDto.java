package com.main.traveltour.dto.guide;

import com.main.traveltour.entity.TourTypes;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * DTO for {@link com.main.traveltour.entity.Tours}
 */
@Data
public class ToursDto implements Serializable {
    String id;

    Integer tourTypeId;

    String tourName;

    String tourImg;

    Timestamp dateCreated;

    Timestamp dateDeleted;

    Boolean isActive;

    String tourDescription;

    TourTypes tourTypesByTourTypeId;
}
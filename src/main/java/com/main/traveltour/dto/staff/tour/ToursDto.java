package com.main.traveltour.dto.staff.tour;

import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * DTO for {@link com.main.traveltour.entity.Tours}
 */
@Data
public class ToursDto {
    String id;

    Integer tourTypeId;

    String tourName;

    Timestamp dateCreated;

    Boolean isActive;

    String tourDescription;
}
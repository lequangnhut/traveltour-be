package com.main.traveltour.dto.staff;

import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * DTO for {@link com.main.traveltour.entity.Tours}
 */
@Data
public class ToursDto implements Serializable {
    int id;

    Integer tourTypeId;

    String tourName;

    String tourImg;

    Timestamp dateCreated;

    Boolean isActive;

    String tourDescription;
}
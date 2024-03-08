package com.main.traveltour.dto.staff.tour;

import com.main.traveltour.entity.TourTypes;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link TourTypes}
 */
@Data
public class TourTypesDto {
    int id;
    String tourTypeName;
}
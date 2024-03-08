package com.main.traveltour.dto.staff.tour;

import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link com.main.traveltour.entity.TourDetailImages}
 */
@Data
public class TourDetailImagesDto implements Serializable {

    int id;

    String tourDetailId;

    String tourDetailImg;
}
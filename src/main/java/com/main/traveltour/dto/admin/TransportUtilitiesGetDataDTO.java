package com.main.traveltour.dto.admin;

import lombok.Data;

/**
 * DTO for {@link com.main.traveltour.entity.TransportUtilities}
 */
@Data
public class TransportUtilitiesGetDataDTO {

    Integer id;

    String icon;

    String title;

    String description;
}

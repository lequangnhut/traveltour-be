package com.main.traveltour.dto.admin.type;

import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link com.main.traveltour.entity.TransportUtilities}
 */
@Data
public class TransportUtilitiesDto implements Serializable {

    Integer id;

    String title;

    String description;
}
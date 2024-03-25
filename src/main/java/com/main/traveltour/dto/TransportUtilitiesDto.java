package com.main.traveltour.dto;

import lombok.Data;
import lombok.Value;

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
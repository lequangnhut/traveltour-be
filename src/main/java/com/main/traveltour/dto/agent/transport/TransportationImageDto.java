package com.main.traveltour.dto.agent.transport;

import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link com.main.traveltour.entity.TransportationImage}
 */
@Data
public class TransportationImageDto implements Serializable {

    int id;

    String transportationId;

    String imagePath;
}
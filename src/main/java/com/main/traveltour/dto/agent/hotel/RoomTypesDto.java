package com.main.traveltour.dto.agent.hotel;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.main.traveltour.entity.RoomTypes}
 */
@Data
public class RoomTypesDto implements Serializable {

    String roomTypeName;

    Integer capacityAdults;

    Integer capacityChildren;

    Integer amountRoom;

    BigDecimal price;

    String roomTypeDescription;

    Integer isActive = 1;

    Boolean isDeleted = false;
}
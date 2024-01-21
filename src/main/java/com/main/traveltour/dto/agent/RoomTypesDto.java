package com.main.traveltour.dto.agent;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.main.traveltour.entity.RoomTypes}
 */
@Data
public class RoomTypesDto implements Serializable {

    String roomTypeName;

    String hotelId;

    Integer capacityAdults;

    Integer capacityChildren;

    Integer amountRoom;

    BigDecimal price;

    String roomTypeDescription;

    Boolean isActive = true;
}
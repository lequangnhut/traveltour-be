package com.main.traveltour.dto.agent.hotel;

import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder

public class RegisterRoomTypeDto {
    private String id;
    private String roomTypeName;
    private String hotelId;
    private Integer capacityAdults;
    private Integer capacityChildren;
    private Integer amountRoom;
    private BigDecimal price;
    private Boolean breakfastIncluded;
    private Boolean freeCancellation;
    private String roomTypeAvatar;
    private String roomTypeDescription;
}

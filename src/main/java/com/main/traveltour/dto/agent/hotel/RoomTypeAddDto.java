package com.main.traveltour.dto.agent.hotel;

import lombok.*;
import org.springframework.data.relational.core.sql.In;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RoomTypeAddDto implements Serializable {
    private String id;
    private String roomTypeName;
    private String hotelId;
    private Integer capacityAdults;
    private Integer capacityChildren;
    private Integer amountRoom;
    private Integer bedTypeId;
    private BigDecimal price;
    private Boolean breakfastIncluded;
    private Boolean freeCancellation;
    private Integer isActive;
    private Boolean isDeleted;
    private Timestamp dateDeleted;
    private String roomTypeAvatar;
    private String roomTypeDescription;
}

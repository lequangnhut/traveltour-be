package com.main.traveltour.dto.agent.hotel;

import com.main.traveltour.entity.*;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class RoomTypeDto {
    private String id;
    private String roomTypeName;
    private String hotelId;
    private Integer capacityAdults;
    private Integer capacityChildren;
    private Integer amountRoom;
    private BigDecimal price;
    private Boolean breakfastIncluded;
    private Boolean freeCancellation;
    private LocalTime checkinTime;
    private LocalTime checkoutTime;
    private Integer isActive;
    private Boolean isDeleted;
    private Timestamp dateDeleted;
    private String roomTypeAvatar;
    private String roomTypeDescription;
    private Collection<OrderHotelDetails> orderHotelDetailsById;
    private Collection<RoomBeds> roomBedsById;
    private Collection<RoomImages> roomImagesById;
    private Hotels hotelsByHotelId;
    private List<RoomUtilities> roomUtilities = new ArrayList<>();
    private Double rate;
    private Integer countRating;
}

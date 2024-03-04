package com.main.traveltour.dto.staff;

import com.main.traveltour.entity.*;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class RoomTypeAvailabilityDto {
    String id;
    String roomTypeName;
    String hotelId;
    Integer capacityAdults;
    Integer capacityChildren;
    Integer amountRoom;
    BigDecimal price;
    Boolean breakfastIncluded;
    Boolean freeCancellation;
    LocalTime checkinTime;
    LocalTime checkoutTime;
    Integer isActive;
    Boolean isDeleted;
    Timestamp dateDeleted;
    String roomTypeAvatar;
    String roomTypeDescription;
    List<RoomUtilities> roomUtilities = new ArrayList<>();
    Integer bookedRooms;
    Integer availableRooms;

    public RoomTypeAvailabilityDto(String id, String roomTypeName, Integer amountRoom, Integer bookedRooms) {
        this.id = id;
        this.roomTypeName = roomTypeName;
        this.amountRoom = amountRoom;
        this.bookedRooms = bookedRooms == null ? 0 : bookedRooms;
        this.availableRooms = amountRoom - this.bookedRooms;
    }

}

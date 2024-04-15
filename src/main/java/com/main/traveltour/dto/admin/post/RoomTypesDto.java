package com.main.traveltour.dto.admin.post;

import com.main.traveltour.entity.Hotels;
import com.main.traveltour.entity.RoomImages;
import com.main.traveltour.entity.RoomUtilities;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

/**
 * DTO for {@link com.main.traveltour.entity.RoomTypes}
 */
@Data
public class RoomTypesDto implements Serializable {
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
    Hotels hotelsByHotelId;
    List<RoomUtilities> roomUtilities;
    Collection<RoomImages> roomImagesById;
}
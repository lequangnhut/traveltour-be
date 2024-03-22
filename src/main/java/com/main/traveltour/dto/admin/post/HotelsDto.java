package com.main.traveltour.dto.admin.post;

import com.main.traveltour.entity.*;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

/**
 * DTO for {@link com.main.traveltour.entity.Hotels}
 */
@Data
public class HotelsDto implements Serializable {
    String id;
    String hotelName;
    String urlWebsite;
    String phone;
    Integer floorNumber;
    String province;
    String district;
    String ward;
    String address;
    Timestamp dateCreated;
    Timestamp dateDeleted;
    Boolean isAccepted;
    Boolean isActive;
    Boolean isDeleted;
    String hotelAvatar;
    String hotelDescription;
    String longitude;
    String latitude;
    int hotelTypeId;
    int agenciesId;
    Agencies agencies;
    HotelTypes hotelTypes;
    List<PlaceUtilities> placeUtilities;
    Collection<RoomTypes> roomTypesById;
}
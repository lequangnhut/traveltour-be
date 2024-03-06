package com.main.traveltour.dto.staff;

import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class HotelsDto {
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
    String longitude;
    String latitude;
    int hotelTypeId;
    int agenciesId;
    String location;
    BigDecimal averageRoomPrice;

}
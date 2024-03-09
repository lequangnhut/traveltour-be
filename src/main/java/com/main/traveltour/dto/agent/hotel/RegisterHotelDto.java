package com.main.traveltour.dto.agent.hotel;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class RegisterHotelDto {
    String id;
    String hotelName;
    String urlWebsite;
    String phone;
    Integer amountRoom;
    Integer floorNumber;
    String province;
    String district;
    String ward;
    String address;
    Boolean isActive;
    Timestamp dateCreated;
    int hotelTypeId;
    int agenciesId;
}

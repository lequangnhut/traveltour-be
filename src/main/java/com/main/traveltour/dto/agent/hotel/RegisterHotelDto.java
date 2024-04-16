package com.main.traveltour.dto.agent.hotel;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class RegisterHotelDto {
    private String id;
    private String hotelName;
    private String urlWebsite;
    private String phone;
    private Integer amountRoom;
    private Integer floorNumber;
    private String province;
    private String district;
    private String ward;
    private String address;
    private Boolean isActive;
    private Timestamp dateCreated;
    private String longitude;
    private String latitude;
    private String hotelDescription;
    int hotelTypeId;
    int agenciesId;
}

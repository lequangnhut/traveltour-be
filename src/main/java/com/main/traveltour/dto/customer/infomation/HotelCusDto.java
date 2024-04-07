package com.main.traveltour.dto.customer.infomation;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.main.traveltour.entity.*;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class HotelCusDto {
    private String id;
    private String hotelName;
    private String urlWebsite;
    private String phone;
    private Integer floorNumber;
    private String province;
    private String district;
    private String ward;
    private String address;
    private Timestamp dateCreated;
    private Timestamp dateDeleted;
    private Boolean isAccepted;
    private Boolean isActive;
    private Boolean isDeleted;
    private String hotelAvatar;
    private String hotelDescription;
    private String longitude;
    private String latitude;
    private int hotelTypeId;
    private int agenciesId;
}

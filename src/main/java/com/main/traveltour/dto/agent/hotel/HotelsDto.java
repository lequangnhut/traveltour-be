package com.main.traveltour.dto.agent.hotel;

import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * DTO for {@link com.main.traveltour.entity.Hotels}
 */
@Data
public class HotelsDto implements Serializable {

    int id;

    String hotelName;

    String urlWebsite;

    String phone;

    Integer amountRoom;

    String province;

    String district;

    String ward;

    String address;

    Timestamp dateCreated;

    Boolean isActive;

    int userId;

    int hotelTypeId;

    int agenciesId;
}
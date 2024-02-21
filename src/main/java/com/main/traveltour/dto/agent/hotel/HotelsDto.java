package com.main.traveltour.dto.agent.hotel;

import com.main.traveltour.entity.PlaceUtilities;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO for {@link com.main.traveltour.entity.Hotels}
 */
@Data
@ToString
public class HotelsDto implements Serializable {

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
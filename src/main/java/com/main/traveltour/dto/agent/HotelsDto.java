package com.main.traveltour.dto.agent;

import com.main.traveltour.entity.PlaceUtilities;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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

    Boolean isAccepted;

    Boolean isActive;

    int hotelTypeId;

    List<PlaceUtilities> placeUtilities = new ArrayList<>();
}
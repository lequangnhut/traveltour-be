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

    Boolean isAccepted; // 0 chưa nộp thông tin, 1 đã nộp thông tin

    Boolean isActive;

    int hotelTypeId;

    int agenciesId;

    List<PlaceUtilities> placeUtilities = new ArrayList<>();
}
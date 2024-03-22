package com.main.traveltour.dto.staff;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class TransportationBrandsDto {

    String id;

    String transportationBrandName;

    int agenciesId;

    Timestamp dateCreated;

    Timestamp dateDeleted;

    Boolean isAccepted;

    Boolean isActive;

    String transportationBrandImg;

    String transportationBrandPolicy;

    String transportationBrandAddress;
}
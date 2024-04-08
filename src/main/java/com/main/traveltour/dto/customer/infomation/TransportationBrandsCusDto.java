package com.main.traveltour.dto.customer.infomation;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class TransportationBrandsCusDto {
    private String id;
    private String transportationBrandName;
    private int agenciesId;
    private Timestamp dateCreated;
    private Timestamp dateDeleted;
    private Boolean isAccepted;
    private Boolean isActive;
    private String transportationBrandAddress;
    private String transportationBrandImg;
    private String transportationBrandPolicy;
}

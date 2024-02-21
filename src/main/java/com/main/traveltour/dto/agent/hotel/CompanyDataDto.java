package com.main.traveltour.dto.agent.hotel;

import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CompanyDataDto {
    private String hotelName;
    private String phoneNumber;
    private String website;
    private String province;
    private String district;
    private String ward;
    private String provinceName;
    private String districtName;
    private String wardName;
    private String address;
    private String floorNumber;
    private Integer hotelType;
    private Integer agencyId;
}

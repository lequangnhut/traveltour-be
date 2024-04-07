package com.main.traveltour.dto.customer.infomation;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.main.traveltour.entity.*;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class VisitLocationsCusDto {
    private String id;
    private String visitLocationName;
    private String visitLocationImage;
    private String urlWebsite;
    private String phone;
    private String province;
    private String district;
    private String ward;
    private String address;
    private Time openingTime;
    private Time closingTime;
    private Timestamp dateCreated;
    private Timestamp dateDeleted;
    private Boolean isAccepted;
    private Boolean isActive;
    private String detailDescription;
    private int visitLocationTypeId;
    private int agenciesId;
}

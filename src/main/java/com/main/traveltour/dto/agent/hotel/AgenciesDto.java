package com.main.traveltour.dto.agent.hotel;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * DTO for {@link com.main.traveltour.entity.Agencies}
 */
@Data
public class AgenciesDto implements Serializable {

    private int id;

    private String nameAgency;

    private String representativeName;

    private String taxId;

    private String urlWebsite;

    private String phone;

    private String province;

    private String district;

    private String ward;

    private String address;

    private Timestamp dateCreated;

    private String noted;

    private Timestamp dateBlocked;

    private Boolean isActive;

    private int isAccepted;

    private int userId;
}
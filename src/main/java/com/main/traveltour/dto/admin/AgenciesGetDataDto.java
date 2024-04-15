package com.main.traveltour.dto.admin;

import com.main.traveltour.entity.Users;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * DTO for {@link com.main.traveltour.entity.Agencies}
 */
@Data
public class AgenciesGetDataDto implements Serializable {

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

    private Timestamp dateBlocked;

    private Timestamp dateAccepted;

    private Integer isAccepted;

    private Boolean isActive;

    private String imgDocument;

    private String noted;

    private int userId;

    private Users usersByUserId;
}
package com.main.traveltour.dto.agent.transport;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * DTO for {@link com.main.traveltour.entity.Agencies}
 */
@Data
public class AgenciesDto implements Serializable {

    int id;
    String nameAgency;

    String representativeName;

    String taxId;

    String urlWebsite;

    String phone;

    String province;

    String district;

    String ward;

    String address;

    Timestamp dateCreated;

    Boolean isActive;

    int userId;

    Integer isAccepted;

    String imgDocument;
}
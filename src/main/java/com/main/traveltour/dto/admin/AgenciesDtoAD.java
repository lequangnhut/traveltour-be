package com.main.traveltour.dto.admin;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class AgenciesDtoAD {

    private int id;

    private String nameAgency;

    private String representativeName;

    private String taxId;

    private String urlWebsite;

    private String phone;

    private String imgDocument;

    private String province;

    private String district;

    private String ward;

    private String address;

    private Timestamp dateCreated;

    private Boolean isActive;

    private int userId;
}

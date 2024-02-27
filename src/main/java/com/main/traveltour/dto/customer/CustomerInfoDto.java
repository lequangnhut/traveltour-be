package com.main.traveltour.dto.customer;

import lombok.Data;

import java.sql.Date;

@Data
public class CustomerInfoDto {

    private int id;

    private String avatar;
    private String email;

    private String password;

    private String fullName;

    private Date birth;

    private String phone;

    private String address;

    private String citizenCard;

    private Integer gender;

    private Boolean isActive;

    private String token;
}

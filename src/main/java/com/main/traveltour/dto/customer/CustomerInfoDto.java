package com.main.traveltour.dto.customer;

import lombok.Data;

import java.sql.Date;

@Data
public class CustomerInfoDto {
    int id;
    String email;
    String password;
    String fullName;
    Date birth;
    String phone;
    String address;
    String citizenCard;
    Integer gender;
    Boolean isActive;
}

package com.main.traveltour.dto.superadmin;

import lombok.Data;

@Data
public class AccountDto {

    private int id;

    private String email;

    private String password;

    private String fullName;

    private String citizenCard;

    private String address;

    private String phone;

    private Boolean isActive;
}

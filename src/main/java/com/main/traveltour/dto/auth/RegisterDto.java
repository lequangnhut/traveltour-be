package com.main.traveltour.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterDto {

    private String email;

    private String password;

    private String fullName;

    private String phone;
}

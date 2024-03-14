package com.main.traveltour.dto.customer.infomation;

import lombok.Data;

@Data
public class ForgotPasswordDto {
    private String email;

    private String verifyCode;

    private String full_name;
}

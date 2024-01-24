package com.main.traveltour.dto.staff;

import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.sql.Date;

/**
 * DTO for {@link com.main.traveltour.entity.Users}
 */
@Data
public class CustomerDto {
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
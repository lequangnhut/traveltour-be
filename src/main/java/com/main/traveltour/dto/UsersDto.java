package com.main.traveltour.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * DTO for {@link com.main.traveltour.entity.Users}
 */
@Data
public class UsersDto implements Serializable {

    int id;

    String email;

    String avatar;

    String fullName;

    Date birth;

    String phone;

    String nationality;

    String citizenCard;

    Integer gender;

    Timestamp dateCreated;

    Boolean isActive;

    String token;
}
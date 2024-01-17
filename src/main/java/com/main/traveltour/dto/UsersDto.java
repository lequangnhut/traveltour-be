package com.main.traveltour.dto;

import com.main.traveltour.entity.Roles;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO for {@link com.main.traveltour.entity.Users}
 */
@Data
public class UsersDto implements Serializable {

    private int id;

    private String email;

    private String avatar;

    private String fullName;

    private Date birth;

    private String phone;

    private String address;

    private String citizenCard;

    private Integer gender;

    private Timestamp dateCreated;

    private Boolean isActive;

    private String token;

    private List<Roles> roles = new ArrayList<>();
}
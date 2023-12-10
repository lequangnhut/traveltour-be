package com.main.traveltour.security.jwt;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JwtUserDetails {

    private String email;

    private List<String> roles;

    public JwtUserDetails(String email, List<String> roles) {
        this.email = email;
        this.roles = roles;
    }
}

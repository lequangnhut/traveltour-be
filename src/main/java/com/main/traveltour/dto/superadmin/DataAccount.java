package com.main.traveltour.dto.superadmin;

import lombok.Data;

import java.util.List;

@Data
public class DataAccount {
    private AccountDto accountDto;

    private List<String> roles;
}

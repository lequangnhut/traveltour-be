package com.main.traveltour.service;

import com.main.traveltour.dto.auth.LoginDto;
import com.main.traveltour.entity.Users;

public interface UsersService {

    Users findByEmail(String email);

    Users findByPhone(String phoneNumber);

    Users findByToken(String token);

    void authenticateRegister(Users users);

    String authenticateLogin(LoginDto loginDto);

    void save(Users users);
}

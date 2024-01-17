package com.main.traveltour.service;

import com.main.traveltour.dto.auth.LoginDto;
import com.main.traveltour.entity.Users;

import java.util.List;

public interface UsersService {

    Users findById(int userId);

    Users findByEmail(String email);

    Users findByPhone(String phoneNumber);

    Users findByToken(String token);

    List<Users> findAllAccountStaff();

    List<Users> findAllAccountAgent();

    List<Users> findDecentralizationStaffByActiveIsTrue();

    List<Users> findDecentralizationAgentByActiveIsTrue();

    void authenticateRegister(Users users);

    String authenticateLogin(LoginDto loginDto);

    Users save(Users users);
}

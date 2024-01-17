package com.main.traveltour.service;

import com.main.traveltour.dto.auth.LoginDto;
import com.main.traveltour.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UsersService {

    Users findById(int userId);

    Users findByEmail(String email);

    Users findByPhone(String phoneNumber);

    Users findByCardId(String cardId);

    Users findByToken(String token);

    Page<Users> findAllAccountStaff(Pageable pageable);

    Page<Users> findAllAccountAgent(Pageable pageable);

    Page<Users> findDecentralizationStaffByActiveIsTrue(Pageable pageable);

    Page<Users> findDecentralizationAgentByActiveIsTrue(Pageable pageable);

    void authenticateRegister(Users users);

    String authenticateLogin(LoginDto loginDto);

    Users save(Users users);
}

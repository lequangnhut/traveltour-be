package com.main.traveltour.service;

import com.main.traveltour.dto.auth.LoginDto;
import com.main.traveltour.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UsersService {

    Users findById(int userId);

    Users findByEmail(String email);

    Users findByPhone(String phoneNumber);

    Users findByCardId(String cardId);

    Users findByToken(String token);

    Page<Users> findAllAccountStaffWithSearch(String searchTerm, Pageable pageable);

    Page<Users> findAllAccountAgentWithSearch(String searchTerm, Pageable pageable);

    Page<Users> findDecentralizationStaff(Pageable pageable);

    Page<Users> findDecentralizationAgent(Pageable pageable);

    void authenticateRegister(Users users);

    String authenticateLogin(LoginDto loginDto);

    Users save(Users users);
}

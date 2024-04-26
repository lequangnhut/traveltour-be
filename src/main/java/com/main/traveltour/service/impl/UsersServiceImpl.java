package com.main.traveltour.service.impl;

import com.main.traveltour.dto.auth.LoginDto;
import com.main.traveltour.entity.Roles;
import com.main.traveltour.entity.Users;
import com.main.traveltour.repository.RolesRepository;
import com.main.traveltour.repository.UsersRepository;
import com.main.traveltour.security.jwt.JwtUtilities;
import com.main.traveltour.service.UsersService;
import com.main.traveltour.utils.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtilities jwtUtilities;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Roles checkRoleExist() {
        Roles role = new Roles();
        role.setNameRole("ROLE_CUSTOMER");
        return rolesRepository.save(role);
    }

    @Override
    public Users findById(int userId) {
        return usersRepository.findUsersById(userId);
    }

    @Override
    public Users findByEmail(String email) {
        return usersRepository.findUserByEmail(email);
    }

    @Override
    public Users findByPhone(String phone) {
        return usersRepository.findByPhone(phone);
    }

    @Override
    public Users findByCardId(String cardId) {
        return usersRepository.findByCitizenCard(cardId);
    }

    @Override
    public Users findByToken(String token) {
        return usersRepository.findByToken(token);
    }

    @Override
    public Page<Users> findAllAccountStaffWithSearch(String searchTerm, Pageable pageable) {
        return usersRepository.searchAccountStaff(searchTerm, pageable);
    }

    @Override
    public Page<Users> findAllAccountAgentWithSearch(String searchTerm, Pageable pageable) {
        return usersRepository.searchAccountAgent(searchTerm, pageable);
    }

    @Override
    public Page<Users> findAllAccountCustomerWithSearch(String searchTerm, Pageable pageable) {
        return usersRepository.searchAccountCustomer(searchTerm, pageable);
    }

    @Override
    public Page<Users> findDecentralizationStaff(Pageable pageable) {
        return usersRepository.findDecentralizationStaff(pageable);
    }

    @Override
    public Page<Users> findDecentralizationAgent(Pageable pageable) {
        return usersRepository.findDecentralizationAgent(pageable);
    }

    @Override
    public Page<Users> findDecentralizationCustomer(Pageable pageable) {
        return usersRepository.findDecentralizationCustomer(pageable);
    }

    @Override
    public void authenticateRegister(Users users) {
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        users.setToken(RandomUtils.RandomToken(20));
        users.setDateCreated(new Timestamp(System.currentTimeMillis()));
        users.setAddress("Việt Nam");

        Roles role = rolesRepository.findByNameRole("ROLE_CUSTOMER");
        if (role == null) {
            role = checkRoleExist();
        }
        users.setRoles(List.of(role));
        usersRepository.save(users);
    }

    @Override
    public String authenticateLogin(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Users user = usersRepository.findByEmail(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        List<String> rolesNames = new ArrayList<>();
        user.getRoles().forEach(r -> rolesNames.add(r.getNameRole()));
        return jwtUtilities.generateToken(user.getEmail(), rolesNames);
    }

    @Override
    public Users save(Users users) {
        return usersRepository.save(users);
    }

    @Override
    public List<Users> findUsersByRolesIsGuild() {
        return usersRepository.findUsersByRolesIsGuild();
    }

    @Override
    public Users checkMailForgot(String email) {
        return usersRepository.findByEmailAndActive(email);
    }

    @Override
    public Users checkMailAdminForgot(String email) {
        return usersRepository.findByEmailAndActiveAdmin(email);
    }

    @Override
    public Long countUsers() {
        return usersRepository.countUsers();
    }

    @Override
    public Integer countUserNow() {
        return usersRepository.countUserNow();
    }

    @Override
    public Integer countUserMonthAgo() {
        return usersRepository.countUserMonthAgo();
    }

    @Override
    public Integer countUserAgentNow() {
        return usersRepository.countUserAgentNow();
    }

    @Override
    public Integer countUserAgentMonthAgo() {
        return usersRepository.countUserAgentMonthAgo();
    }
}

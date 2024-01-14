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
    public Users findByEmail(String email) {
        return usersRepository.findUserByEmail(email);
    }

    @Override
    public Users findByPhone(String phone) {
        return usersRepository.findByPhone(phone);
    }

    @Override
    public Users findByToken(String token) {
        return usersRepository.findByToken(token);
    }

    @Override
    public void authenticateRegister(Users users) {
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        users.setToken(RandomUtils.RandomToken(20));
        users.setDateCreated(new Timestamp(System.currentTimeMillis()));

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
    public void save(Users users) {
        usersRepository.save(users);
    }
}

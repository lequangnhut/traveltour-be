package com.main.traveltour.service.customer.impl;

import com.main.traveltour.entity.Users;
import com.main.traveltour.repository.UsersRepository;
import com.main.traveltour.service.customer.UserServiceCT;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserServiceCTImpl implements UserServiceCT {

    @Autowired
    private UsersRepository usersRepo;


    @Override
    public Integer findIdByPhoneNumberAndNotCurrentUser(String phone, Integer userID) {
        return usersRepo.findIdByPhoneNumberAndNotCurrentUser(phone,userID);
    }

    @Override
    public Users findByID(int id) {
        return usersRepo.findUsersById(id);
    }
}

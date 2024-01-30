package com.main.traveltour.service.customer;

import com.main.traveltour.entity.Users;
import org.springframework.security.core.userdetails.User;

public interface UserServiceCT {

   Integer findIdByPhoneNumberAndNotCurrentUser(String phone, Integer userID);

   Users findByID(int id);


}

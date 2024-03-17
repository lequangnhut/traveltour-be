package com.main.traveltour.service.customer;

import com.main.traveltour.entity.PassOTP;

public interface PassOTPService {

    PassOTP findByUserIdAndToken(int id, String token);

    void save(PassOTP passOTP);

    void deactivateOldPassOTP(int userId);

    PassOTP findByToken(String token);

    PassOTP findByOTPAndEmail(String codeOTP, String email);

    void updateActive();
}

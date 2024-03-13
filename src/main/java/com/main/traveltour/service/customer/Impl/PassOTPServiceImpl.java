package com.main.traveltour.service.customer.Impl;

import com.main.traveltour.entity.PassOTP;
import com.main.traveltour.repository.PassOTPRepository;
import com.main.traveltour.service.customer.PassOTPService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class PassOTPServiceImpl implements PassOTPService {

    @Autowired
    PassOTPRepository passOTPRepository;

    @Override
    public PassOTP findByUserIdAndToken(int id, String token) {
        return passOTPRepository.findByUsersIdAndCodetokenAndIsActiveIsTrue(id, token);
    }

    @Override
    public void save(PassOTP passOTP) {
        passOTPRepository.save(passOTP);
    }

    @Override
    @Transactional
    public void deactivateOldPassOTP(int userId) {
        List<PassOTP> oldPassOTPs = passOTPRepository.findActivePassOTPsByUserId(userId);

        for (PassOTP oldPassOTP : oldPassOTPs) {
            oldPassOTP.setIsActive(false);
            passOTPRepository.save(oldPassOTP);
        }
    }

    @Override
    public PassOTP findByToken(String token) {
        return passOTPRepository.findByCodetoken(token);
    }

    @Override
    public void updateActive() {
        List<PassOTP> findTrue = passOTPRepository.findByIsActiveIsTrue();
        for (PassOTP passOTP : findTrue) {

            Instant instantDateCreated = passOTP.getDateCreated().toInstant();
            Instant instantNow = Instant.now();

            long diffInSeconds = instantNow.getEpochSecond() - instantDateCreated.getEpochSecond();

            if (diffInSeconds >= 600) {
                passOTP.setIsActive(false);
                passOTPRepository.save(passOTP);
            }
        }
    }
}

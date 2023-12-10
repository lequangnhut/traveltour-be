package com.main.traveltour.service;

import com.main.traveltour.dto.auth.RegisterDto;

public interface EmailService {

    void queueEmailRegister(RegisterDto registerDto);

    void sendMailRegister();
}

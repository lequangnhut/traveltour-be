package com.main.traveltour.service.utils;

import com.main.traveltour.dto.auth.RegisterDto;

public interface EmailService {

    void queueEmailRegister(RegisterDto registerDto);

    void sendMailRegister();
}

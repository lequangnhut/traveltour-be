package com.main.traveltour.service.utils;

import com.main.traveltour.dto.agent.AgenciesDto;
import com.main.traveltour.dto.auth.RegisterDto;
import com.main.traveltour.dto.superadmin.DataAccountDto;

public interface EmailService {

    void queueEmailRegister(RegisterDto registerDto);

    void sendMailRegister();

    void queueEmailCreateBusiness(DataAccountDto dataAccountDto);

    void sendMailCreateBusiness();

    void queueEmailRegisterAgency(AgenciesDto agenciesDto);

    void sendMailRegisterAgency();
}

package com.main.traveltour.service.utils;

import com.main.traveltour.dto.agent.hotel.AgenciesDto;
import com.main.traveltour.dto.auth.RegisterDto;
import com.main.traveltour.dto.customer.booking.BookingDto;
import com.main.traveltour.dto.superadmin.DataAccountDto;

public interface EmailService {

    void queueEmailRegister(RegisterDto registerDto);

    void sendMailRegister();

    void queueEmailCreateBusiness(DataAccountDto dataAccountDto);

    void sendMailCreateBusiness();

    void queueEmailRegisterAgency(AgenciesDto agenciesDto);

    void sendMailRegisterAgency();

    void queueEmailAcceptedAgency(AgenciesDto agenciesDto);

    void sendMailAcceptedAgency();

    void queueEmailDeniedAgency(AgenciesDto agenciesDto);

    void sendMailDeniedAgency();

    void queueEmailBookingTour(BookingDto bookingDto);

    void sendMailBookingTour();
}

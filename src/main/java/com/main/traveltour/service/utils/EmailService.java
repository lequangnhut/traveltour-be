package com.main.traveltour.service.utils;

import com.main.traveltour.dto.agent.hotel.AgenciesDto;
import com.main.traveltour.dto.agent.transport.OrderTransportationsDto;
import com.main.traveltour.dto.auth.RegisterDto;
import com.main.traveltour.dto.customer.booking.BookingDto;
import com.main.traveltour.dto.customer.infomation.*;
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

    void sendMailForgot();

    void queueEmailForgot(ForgotPasswordDto passwordsDto);

    void sendMailCustomerCancelTour();

    void queueEmailCustomerCancelTour(CancelBookingTourDTO bookingToursDto);

    void sendMailOTPCus();

    void queueEmailOTPCus(ForgotPasswordDto passwordDto);

    void queueEmailBookingTourInvoices(BookingDto bookingDto);

    void sendMailBookingTourInvoices();

    void sendMailCustomerCancelHotel();

    void queueEmailCustomerCancelHotel(CancelOrderHotelsDto cancelOrderHotelsDto);

    void sendMailCustomerCancelVisit();

    void queueEmailCustomerCancelVisit(OrderVisitsDto orderVisitsDto);

    void sendMailCustomerCancelTrans();

    void queueEmailCustomerCancelTrans(CancelOrderTransportationsDto cancelOrderTransportationsDto);

    void sendMailCustomerBookingTransport();

    void queueEmailCustomerBookingTransport(OrderTransportationsDto orderTransportationsDto);
}

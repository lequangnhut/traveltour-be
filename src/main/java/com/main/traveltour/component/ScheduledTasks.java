package com.main.traveltour.component;

import com.main.traveltour.service.agent.TransportScheduleSeatService;
import com.main.traveltour.service.agent.TransportationScheduleService;
import com.main.traveltour.service.customer.PassOTPService;
import com.main.traveltour.service.staff.OrderHotelsService;
import com.main.traveltour.service.staff.TourDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class ScheduledTasks {

    @Autowired
    private TransportationScheduleService transportationScheduleService;

    @Autowired
    private TransportScheduleSeatService transportScheduleSeatService;

    @Autowired
    private TourDetailsService tourDetailsService;

    @Autowired
    private PassOTPService passOTPService;

    @Autowired
    private OrderHotelsService orderHotelsService;

    @Scheduled(fixedDelay = 5000)
    public void checkTransportSchedulesStatus() {
        transportationScheduleService.updateStatusAndActive();
    }

    @Scheduled(fixedDelay = 5000)
    public void checkTourDetailStatus() {
        tourDetailsService.updateStatusAndActive();
    }

    @Scheduled(fixedDelay = 5000)
    public void checkFailOtpForgot() {
        passOTPService.updateActive();
    }

    @Scheduled(fixedDelay = 1000)
    public void checkTimeBookingTransport() {
        transportScheduleSeatService.checkTimeBookingTransport();
    }

    @Scheduled(fixedDelay = 5000)
    public void checkOrderHotelExpires() {
        orderHotelsService.checkOrderHotelExpires();
    }
}

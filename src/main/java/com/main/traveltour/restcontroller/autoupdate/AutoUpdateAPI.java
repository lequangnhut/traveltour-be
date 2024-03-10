package com.main.traveltour.restcontroller.autoupdate;

import com.main.traveltour.service.agent.TransportationScheduleService;
import com.main.traveltour.service.customer.PassOTPService;
import com.main.traveltour.service.staff.TourDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/update-auto/")
public class AutoUpdateAPI {

    @Autowired
    PassOTPService passOTPService;

    @Autowired
    TourDetailsService tourDetailsService;

    @Autowired
    TransportationScheduleService transportationScheduleService;


    @GetMapping("forgot-token-false")
    private void checkTokenOutTime() {
        passOTPService.updateActive();
    }

    @GetMapping("tour-details-status")
    private void checkTourStatus() {
        tourDetailsService.updateStatusAndActive();
    }

    @GetMapping("transportation-schedules-status")
    private void checkScheduleStatus() {
        transportationScheduleService.updateStatusAndActive();
    }
}

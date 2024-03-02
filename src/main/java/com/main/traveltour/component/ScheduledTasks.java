package com.main.traveltour.component;

import com.main.traveltour.service.agent.TransportationScheduleService;
import com.main.traveltour.service.staff.staff.TourDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    @Autowired
    private TransportationScheduleService transportationScheduleService;

    @Autowired
    private TourDetailsService tourDetailsService;

    @Scheduled(fixedDelay = 1000)
    public void checkTransportSchedulesStatus() {
        transportationScheduleService.updateStatusAndActive();
    }

    @Scheduled(fixedDelay = 1000)
    public void checkTourDetailStatus() {
        tourDetailsService.updateStatusAndActive();
    }
}

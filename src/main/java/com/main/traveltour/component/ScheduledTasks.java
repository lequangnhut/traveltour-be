package com.main.traveltour.component;

import com.main.traveltour.service.agent.TransportationScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    @Autowired
    private TransportationScheduleService transportationScheduleService;

    @Scheduled(fixedDelay = 1000)
    public void checkTransportSchedulesStatus() {
        transportationScheduleService.updateStatusAndActive();
    }
}

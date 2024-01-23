package com.main.traveltour.restcontroller.agent;

import com.main.traveltour.service.agent.TransportationScheduleService;
import com.main.traveltour.service.agent.TransportationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1")
public class TransportationScheduleAPI {

    @Autowired
    private TransportationService transportationService;

    @Autowired
    private TransportationScheduleService transportationScheduleService;
}

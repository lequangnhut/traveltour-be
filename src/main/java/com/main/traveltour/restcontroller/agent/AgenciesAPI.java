package com.main.traveltour.restcontroller.agent;

import com.main.traveltour.entity.Agencies;
import com.main.traveltour.service.agent.AgenciesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1")
public class AgenciesAPI {

    @Autowired
    private AgenciesService agenciesService;

    @GetMapping("/agent/agencies/find-by-user-id/{userId}")
    private Agencies findAllAccountStaff(@PathVariable int userId) {
        return agenciesService.findByUserId(userId);
    }
}

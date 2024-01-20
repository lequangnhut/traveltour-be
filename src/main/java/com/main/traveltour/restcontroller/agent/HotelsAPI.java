package com.main.traveltour.restcontroller.agent;

import com.main.traveltour.entity.Hotels;
import com.main.traveltour.service.agent.HotelsService;
import com.main.traveltour.service.utils.FileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1")
public class HotelsAPI {

    @Autowired
    private FileUpload fileUpload;

    @Autowired
    private HotelsService hotelsService;

    @GetMapping("/agent/hotels/find-by-agency-id/{userId}")
    private Hotels findByUserId(@PathVariable int userId) {
        return hotelsService.findByAgencyId(userId);
    }
}

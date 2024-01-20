package com.main.traveltour.restcontroller.agent;

import com.main.traveltour.dto.agent.VisitLocationsDto;
import com.main.traveltour.entity.VisitLocationTypes;
import com.main.traveltour.entity.VisitLocations;
import com.main.traveltour.service.agent.VisitLocationTypeService;
import com.main.traveltour.service.agent.VisitLocationsService;
import com.main.traveltour.service.utils.FileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1")
public class VisitLocationsAPI {

    @Autowired
    private FileUpload fileUpload;

    @Autowired
    private VisitLocationsService visitLocationsService;

    @Autowired
    private VisitLocationTypeService visitLocationTypeService;

    @GetMapping("/agent/visit/find-visit-type-for-register-agency")
    private List<VisitLocationTypes> findAll() {
        return visitLocationTypeService.findAllForRegisterAgency();
    }

    @GetMapping("/agent/visit/find-by-agency-id/{userId}")
    private VisitLocations findByUserId(@PathVariable int userId) {
        return visitLocationsService.findByAgencyId(userId);
    }

    @PostMapping("/agent/visit/register-visit-location")
    private void registerVisitLocation(
            @RequestPart("visitLocationsDto") VisitLocationsDto visitLocationsDto,
            @RequestPart("visitLocationImage") MultipartFile visitImage,
            @RequestPart(value = "selectedTickets", required = false) List<String> selectedTickets,
            @RequestPart(value = "unitPrices", required = false) Map<String, String> unitPrices) {
        System.out.println(visitLocationsDto);
        System.out.println(visitImage);
        System.out.println(selectedTickets);
        System.out.println(unitPrices);
    }
}

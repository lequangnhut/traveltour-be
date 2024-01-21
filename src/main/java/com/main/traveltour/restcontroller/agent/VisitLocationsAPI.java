package com.main.traveltour.restcontroller.agent;

import com.main.traveltour.dto.agent.VisitLocationsDto;
import com.main.traveltour.entity.VisitLocationTickets;
import com.main.traveltour.entity.VisitLocationTypes;
import com.main.traveltour.entity.VisitLocations;
import com.main.traveltour.service.agent.VisitLocationTicketService;
import com.main.traveltour.service.agent.VisitLocationTypeService;
import com.main.traveltour.service.agent.VisitLocationsService;
import com.main.traveltour.service.utils.FileUpload;
import com.main.traveltour.utils.GenerateNextID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
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

    @Autowired
    private VisitLocationTicketService visitLocationTicketService;

    @GetMapping("/agent/visit/find-visit-type-for-register-agency")
    private List<VisitLocationTypes> findAll() {
        return visitLocationTypeService.findAllForRegisterAgency();
    }

    @GetMapping("/agent/visit/find-by-agency-id/{userId}")
    private VisitLocations findByUserId(@PathVariable int userId) {
        return visitLocationsService.findByAgencyId(userId);
    }

    @PostMapping("/agent/visit/register-visit-location")
    private void registerVisitLocation(@RequestPart("visitLocationsDto") VisitLocationsDto visitLocationsDto, @RequestPart("visitLocationImage") MultipartFile visitImage, @RequestPart(value = "selectedTickets", required = false) List<String> selectedTickets, @RequestPart(value = "unitPrices", required = false) Map<String, String> unitPrices) throws IOException {
        int agencyId = visitLocationsDto.getId();
        String visitLocationImage = fileUpload.uploadFile(visitImage);

        VisitLocations visitLocations = visitLocationsService.findByAgencyId(agencyId);
        visitLocations.setVisitLocationName(visitLocationsDto.getVisitLocationName());
        visitLocations.setVisitLocationImage(visitLocationImage);
        visitLocations.setUrlWebsite(visitLocationsDto.getUrlWebsite());
        visitLocations.setPhone(visitLocationsDto.getPhone());
        visitLocations.setProvince(visitLocationsDto.getProvince());
        visitLocations.setDistrict(visitLocationsDto.getDistrict());
        visitLocations.setWard(visitLocationsDto.getWard());
        visitLocations.setAddress(visitLocationsDto.getAddress());
        visitLocations.setOpeningTime(visitLocationsDto.getOpeningTime());
        visitLocations.setClosingTime(visitLocationsDto.getClosingTime());
        visitLocations.setVisitLocationTypeId(visitLocationsDto.getVisitLocationTypeId());
        visitLocations.setIsAccepted(Boolean.TRUE);
        visitLocationsService.save(visitLocations);

        createVisitTicket(visitLocations.getId(), selectedTickets, unitPrices);
    }

    private void createVisitTicket(String locationId, List<String> ticketTypes, Map<String, String> unitPrices) {
        Map<String, String> vietnameseToEnglishMap = new HashMap<>();
        vietnameseToEnglishMap.put("Vé người lớn", "adult");
        vietnameseToEnglishMap.put("Vé trẻ em", "child");

        for (String ticketType : ticketTypes) {
            String ticketId = GenerateNextID.generateNextCode("TK", visitLocationsService.findMaxCode());
            String englishTicketType = vietnameseToEnglishMap.get(ticketType);

            VisitLocationTickets tickets = new VisitLocationTickets();
            tickets.setId(ticketId);
            tickets.setVisitLocationId(locationId);
            tickets.setTicketTypeName(ticketType);

            if (unitPrices.containsKey(englishTicketType)) {
                String unitPriceString = unitPrices.get(englishTicketType);
                BigDecimal unitPrice = new BigDecimal(unitPriceString);
                tickets.setUnitPrice(unitPrice);
            }
            visitLocationTicketService.save(tickets);
        }
    }
}

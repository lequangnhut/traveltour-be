package com.main.traveltour.restcontroller.agent.visitlocation;

import com.main.traveltour.dto.agent.visit_location.VisitLocationsDto;
import com.main.traveltour.entity.*;
import com.main.traveltour.service.agent.VisitLocationTicketService;
import com.main.traveltour.service.agent.VisitLocationTypeService;
import com.main.traveltour.service.agent.VisitLocationsService;
import com.main.traveltour.service.utils.FileUpload;
import com.main.traveltour.utils.EntityDtoUtils;
import com.main.traveltour.utils.GenerateNextID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
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

    @GetMapping("/agent/visit/find-all-by-agency-id/{agencyId}")
    private List<VisitLocations> findByUserId(@PathVariable int agencyId) {
        return visitLocationsService.findAllByAgencyId(agencyId);
    }

    @GetMapping("/agent/visit/find-by-visit-location-id/{visitLocationId}")
    private ResponseObject findTransportBrandById(@PathVariable String visitLocationId) {
        VisitLocations visitLocations = visitLocationsService.findByVisitLocationId(visitLocationId);

        if (visitLocations == null) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", visitLocations);
        }
    }

    @PostMapping("/agent/visit/register-visit-location")
    private void registerVisitLocation(@RequestPart("visitLocationsDto") VisitLocationsDto visitLocationsDto,
                                       @RequestPart("visitLocationImage") MultipartFile visitImage,
                                       @RequestPart(value = "selectedTickets", required = false) List<String> selectedTickets,
                                       @RequestPart(value = "unitPrices", required = false) Map<String, String> unitPrices) throws IOException {
        int agencyId = visitLocationsDto.getAgenciesId();
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
        visitLocations.setDetailDescription(visitLocationsDto.getDetailDescription());
        visitLocations.setIsAccepted(Boolean.TRUE);
        visitLocationsService.save(visitLocations);

        createVisitTicket(visitLocations.getId(), selectedTickets, unitPrices);
    }

    @PostMapping("/agent/visit/create-visit-location")
    private void createVisitLocation(@RequestPart("visitLocationsDto") VisitLocationsDto visitLocationsDto,
                                     @RequestPart("visitLocationImage") MultipartFile visitImage,
                                     @RequestPart(value = "selectedTickets", required = false) List<String> selectedTickets,
                                     @RequestPart(value = "unitPrices", required = false) Map<String, String> unitPrices) throws IOException {

        String visitLocationImage = fileUpload.uploadFile(visitImage);
        String visitId = GenerateNextID.generateNextCode("PLA", visitLocationsService.findMaxCode());

        VisitLocations visitLocations = EntityDtoUtils.convertToEntity(visitLocationsDto, VisitLocations.class);
        visitLocations.setId(visitId);
        visitLocations.setVisitLocationImage(visitLocationImage);
        visitLocations.setDateCreated(new Timestamp(System.currentTimeMillis()));
        visitLocations.setIsAccepted(Boolean.TRUE);
        visitLocations.setIsActive(Boolean.TRUE);
        visitLocationsService.save(visitLocations);

        createVisitTicket(visitLocations.getId(), selectedTickets, unitPrices);
    }

    @PutMapping("/agent/visit/update-visit-location")
    private void updateVisitLocation(@RequestPart("visitLocationsDto") VisitLocationsDto visitLocationsDto,
                                     @RequestPart(value = "visitLocationImage", required = false) MultipartFile visitImage,
                                     @RequestPart(value = "selectedTickets", required = false) List<String> selectedTickets,
                                     @RequestPart(value = "unitPrices", required = false) Map<String, String> unitPrices) throws IOException {
        String visitLocationId = visitLocationsDto.getId();

        if (visitImage != null) {
            String visitLocationImage = fileUpload.uploadFile(visitImage);
            VisitLocations visitLocations = EntityDtoUtils.convertToEntity(visitLocationsDto, VisitLocations.class);
            visitLocations.setId(visitLocationId);
            visitLocations.setVisitLocationImage(visitLocationImage);
            visitLocationsService.save(visitLocations);
        } else {
            VisitLocations visitLocations = EntityDtoUtils.convertToEntity(visitLocationsDto, VisitLocations.class);
            visitLocations.setId(visitLocationId);
            visitLocations.setVisitLocationImage(visitLocationsService.findByVisitLocationId(visitLocationId).getVisitLocationImage());
            visitLocationsService.save(visitLocations);
        }
    }

    @GetMapping("/agent/visit/delete-visit-location/{visitLocationId}")
    private void deleteVisitLocation(@PathVariable String visitLocationId) {
        VisitLocations visitLocations = visitLocationsService.findByVisitLocationId(visitLocationId);
        visitLocations.setIsActive(Boolean.FALSE);
        visitLocations.setDateDeleted(new Timestamp(System.currentTimeMillis()));
        visitLocationsService.save(visitLocations);
    }

    private void createVisitTicket(String locationId, List<String> ticketTypes, Map<String, String> unitPrices) {
        Map<String, String> vietnameseToEnglishMap = new HashMap<>();
        vietnameseToEnglishMap.put("Vé người lớn", "adult");
        vietnameseToEnglishMap.put("Vé trẻ em", "child");

        for (String ticketType : ticketTypes) {
            String englishTicketType = vietnameseToEnglishMap.get(ticketType);

            VisitLocationTickets tickets = new VisitLocationTickets();
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

    private void updateVisitTickets(String locationId, List<String> newTicketTypes, Map<String, String> newUnitPrices) {
        List<VisitLocationTickets> currentTickets = visitLocationTicketService.findByVisitLocationId(locationId);

        Map<String, String> vietnameseToEnglishMap = new HashMap<>();
        vietnameseToEnglishMap.put("Vé người lớn", "adult");
        vietnameseToEnglishMap.put("Vé trẻ em", "child");

        for (String newTicketType : newTicketTypes) {
            String englishTicketType = vietnameseToEnglishMap.get(newTicketType);

            VisitLocationTickets currentTicket = findTicketByType(currentTickets, newTicketType);

            if (currentTicket != null) {
                if (newUnitPrices.containsKey(englishTicketType)) {
                    String unitPriceString = newUnitPrices.get(englishTicketType);
                    BigDecimal unitPrice = new BigDecimal(unitPriceString);
                    currentTicket.setUnitPrice(unitPrice);
                } else {
                    visitLocationTicketService.delete(currentTicket);
                }
            } else {
                VisitLocationTickets newTicket = new VisitLocationTickets();
                newTicket.setVisitLocationId(locationId);
                newTicket.setTicketTypeName(newTicketType);

                if (newUnitPrices.containsKey(englishTicketType)) {
                    String unitPriceString = newUnitPrices.get(englishTicketType);
                    BigDecimal unitPrice = new BigDecimal(unitPriceString);
                    newTicket.setUnitPrice(unitPrice);
                }

                visitLocationTicketService.save(newTicket);
            }
        }
    }

    private VisitLocationTickets findTicketByType(List<VisitLocationTickets> tickets, String ticketType) {
        for (VisitLocationTickets ticket : tickets) {
            if (ticket.getTicketTypeName().equals(ticketType)) {
                return ticket;
            }
        }
        return null;
    }
}

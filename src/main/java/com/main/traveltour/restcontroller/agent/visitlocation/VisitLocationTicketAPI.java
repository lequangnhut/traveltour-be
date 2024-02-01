package com.main.traveltour.restcontroller.agent.visitlocation;

import com.main.traveltour.dto.agent.visit_location.VisitLocationTicketsDto;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.entity.VisitLocationTickets;
import com.main.traveltour.entity.VisitLocations;
import com.main.traveltour.service.agent.VisitLocationTicketService;
import com.main.traveltour.service.agent.VisitLocationsService;
import com.main.traveltour.utils.EntityDtoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1")
public class VisitLocationTicketAPI {

    @Autowired
    private VisitLocationsService visitLocationsService;

    @Autowired
    private VisitLocationTicketService visitLocationTicketService;

    @GetMapping("/agent/visit-location-ticket/find-all-visit-ticket/{brandId}")
    private ResponseEntity<Page<VisitLocationTickets>> findAllTransportBrand(@RequestParam(defaultValue = "0") int page,
                                                                             @RequestParam(defaultValue = "10") int size,
                                                                             @RequestParam(defaultValue = "id") String sortBy,
                                                                             @RequestParam(defaultValue = "asc") String sortDir,
                                                                             @RequestParam(required = false) String searchTerm,
                                                                             @PathVariable String brandId) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Page<VisitLocationTickets> transportationBrands = searchTerm == null || searchTerm.isEmpty()
                ? visitLocationTicketService.findAllVisitTickets(brandId, PageRequest.of(page, size, sort))
                : visitLocationTicketService.findAllWithSearchVisitTickets(brandId, searchTerm, PageRequest.of(page, size, sort));
        return new ResponseEntity<>(transportationBrands, HttpStatus.OK);
    }

    @GetMapping("/agent/visit-location-ticket/find-all-by-visit-locationId/{visitLocationId}")
    private ResponseObject findByVisitLocationId(@PathVariable String visitLocationId) {
        List<VisitLocations> visitLocations = visitLocationsService.findAllByVisitLocationId(visitLocationId);

        if (visitLocations == null) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", visitLocations);
        }
    }

    @GetMapping("/agent/visit-location-ticket/find-by-visit-ticketId/{visitTicketId}")
    private ResponseObject findByVisitTicketId(@PathVariable int visitTicketId) {
        VisitLocationTickets visitLocationTickets = visitLocationTicketService.findByVisitTicketId(visitTicketId);

        if (visitLocationTickets == null) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", visitLocationTickets);
        }
    }

    @PostMapping("/agent/visit-location-ticket/create-visit-ticket")
    private void createVisitTicket(@RequestBody VisitLocationTicketsDto visitLocationTicketsDto) {
        VisitLocationTickets visitLocationTickets = EntityDtoUtils.convertToEntity(visitLocationTicketsDto, VisitLocationTickets.class);
        visitLocationTicketService.save(visitLocationTickets);
    }

    @PutMapping("/agent/visit-location-ticket/update-visit-ticket")
    private void updateVisitTicket(@RequestBody VisitLocationTicketsDto visitLocationTicketsDto) {
        VisitLocationTickets visitLocationTickets = EntityDtoUtils.convertToEntity(visitLocationTicketsDto, VisitLocationTickets.class);
        visitLocationTickets.setId(visitLocationTicketsDto.getId());
        visitLocationTicketService.save(visitLocationTickets);
    }

    @GetMapping("/agent/visit-location-ticket/delete-visit-ticket/{visitTicketId}")
    private void deleteVisitTicket(@PathVariable int visitTicketId) {
        VisitLocationTickets visitLocationTickets = visitLocationTicketService.findByVisitTicketId(visitTicketId);
        visitLocationTicketService.delete(visitLocationTickets);
    }
}

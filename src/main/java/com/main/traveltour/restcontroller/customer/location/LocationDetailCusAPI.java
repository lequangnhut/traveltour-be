package com.main.traveltour.restcontroller.customer.location;

import com.main.traveltour.dto.customer.visit.VisitLocationTrendDTO;
import com.main.traveltour.dto.staff.VisitLocationsDto;
import com.main.traveltour.entity.*;
import com.main.traveltour.service.agent.VisitLocationTicketService;
import com.main.traveltour.service.agent.VisitLocationTypeService;
import com.main.traveltour.service.staff.VisitLocationService;
import com.main.traveltour.utils.EntityDtoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/customer/location-detail/")
public class LocationDetailCusAPI {

    @Autowired
    private VisitLocationService visitLocationService;

    @Autowired
    private VisitLocationTicketService visitLocationTicketService;

    @GetMapping("find-all-location")
    private ResponseObject findAllLocation(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size,
                                           @RequestParam(required = false) String searchTerm) {
        Page<VisitLocations> visitLocations = searchTerm != null && !searchTerm.isEmpty() ? visitLocationService.findBySearchTerm(searchTerm, PageRequest.of(page, size)) : visitLocationService.getAllByIsActiveIsTrueAndIsAcceptedIsTrue(PageRequest.of(page, size));

        Page<VisitLocationsDto> visitLocationsDto = visitLocations.map(visitLocation -> EntityDtoUtils.convertToDto(visitLocation, VisitLocationsDto.class));

        if (visitLocationsDto.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", visitLocationsDto);
        }
    }

    @GetMapping("find-by-id/{id}")
    private ResponseObject findAllLocationType(@PathVariable String id) {
        VisitLocations visitLocations = visitLocationService.findByIdAndIsActiveIsTrue(id);

        VisitLocationsDto visitLocationsDto = EntityDtoUtils.convertToDto(visitLocations, VisitLocationsDto.class);

        if (visitLocationsDto == null) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", visitLocationsDto);
        }
    }

    @GetMapping("find-all-trend")
    private ResponseObject findAllTourTrend() {
        List<VisitLocationTrendDTO> visitLocationsTrend = visitLocationService.findVisitLocationsTrend();

        for (VisitLocationTrendDTO trend : visitLocationsTrend) {
            Collection<VisitLocationTickets> tickets = visitLocationTicketService.findByVisitLocationId(trend.getVisitLocationId());
            trend.setVisitLocationTicketsById(tickets);
        }
        if (visitLocationsTrend.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", visitLocationsTrend);
        }
    }

}

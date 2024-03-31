package com.main.traveltour.restcontroller.customer.location;

import com.main.traveltour.dto.staff.VisitLocationsDto;
import com.main.traveltour.entity.*;
import com.main.traveltour.service.agent.VisitLocationTypeService;
import com.main.traveltour.service.staff.VisitLocationService;
import com.main.traveltour.utils.EntityDtoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/")
public class LocationCusAPI {

    @Autowired
    private VisitLocationService visitLocationService;

    @Autowired
    private VisitLocationTypeService visitLocationTypeService;

    @GetMapping("customer/location/find-all-location")
    private ResponseObject findAllLocation(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size,
                                           @RequestParam(required = false) String searchTerm) {
        Page<VisitLocations> visitLocations = searchTerm != null && !searchTerm.isEmpty() ?
                visitLocationService.findBySearchTerm(searchTerm, PageRequest.of(page, size)) :
                visitLocationService.getAllByIsActiveIsTrueAndIsAcceptedIsTrue(PageRequest.of(page, size));

        Page<VisitLocationsDto> visitLocationsDto = visitLocations.map(visitLocation -> EntityDtoUtils.convertToDto(visitLocation, VisitLocationsDto.class));

        if (visitLocationsDto.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", visitLocationsDto);
        }
    }

    @GetMapping("customer/location/find-all-location-by-filters")
    private ResponseObject findAllLocationByFilters(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) BigDecimal price,
            @RequestParam(required = false) List<String> TickerTypeList,
            @RequestParam(required = false) List<Integer> LocationTypeList) {

        Page<VisitLocations> visitLocations = visitLocationService.findByFilters(searchTerm, price, TickerTypeList, LocationTypeList, PageRequest.of(page, size));

        Page<VisitLocationsDto> visitLocationsDto = visitLocations.map(visitLocation -> EntityDtoUtils.convertToDto(visitLocation, VisitLocationsDto.class));

        if (visitLocationsDto.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", visitLocationsDto);
        }
    }

    @GetMapping("customer/location/find-all-location-type")
    private ResponseObject findAllLocationType() {
        List<VisitLocationTypes> visitLocationTypes = visitLocationTypeService.findAllForRegisterAgency();

        if (visitLocationTypes.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", visitLocationTypes);
        }
    }

    @GetMapping("customer/location/get-location-customer-data-list")
    private ResponseObject getAllDataList() {
        Map<String, Object> response = new HashMap<>();

        List<VisitLocations> visitLocations = visitLocationService.getAllVisitLocation();

        List<String> uniqueDataList = visitLocations.stream().flatMap(visitLocation -> {
            Set<String> visit = new LinkedHashSet<>();
            visit.add(visitLocation.getAgenciesByAgenciesId().getNameAgency());
            visit.add(visitLocation.getVisitLocationName());
            visit.add(visitLocation.getDistrict());
            visit.add(visitLocation.getProvince());
            visit.add(visitLocation.getDistrict() + " - " +
                    visitLocation.getProvince());
            visit.add(visitLocation.getWard() + " - " +
                    visitLocation.getDistrict() + " - " +
                    visitLocation.getProvince());
            visitLocation.getVisitLocationTicketsById().forEach(item -> {
                visit.add(item.getTicketTypeName());
                visit.add(String.valueOf(item.getUnitPrice()));
            });
            return visit.stream();
        }).distinct().collect(Collectors.toList());

        response.put("uniqueDataList", uniqueDataList);

        if (uniqueDataList.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", response);
        }
    }
}

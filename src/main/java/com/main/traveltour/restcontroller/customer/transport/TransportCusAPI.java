package com.main.traveltour.restcontroller.customer.transport;

import com.main.traveltour.dto.customer.TransportationBrandsDto;
import com.main.traveltour.dto.customer.transport.TransportationSchedulesDto;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.entity.TransportationBrands;
import com.main.traveltour.entity.TransportationSchedules;
import com.main.traveltour.service.agent.TransportationBrandsService;
import com.main.traveltour.service.agent.TransportationScheduleService;
import com.main.traveltour.utils.EntityDtoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/")
public class TransportCusAPI {

    @Autowired
    private TransportationScheduleService transportationScheduleService;

    @Autowired
    private TransportationBrandsService transportationBrandsService;

    @GetMapping("customer/transport/find-all-transport-brand")
    public ResponseObject findAllTransportCus(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "9") int size) {
        Page<TransportationBrands> brandsPage = transportationBrandsService.findAllCus(PageRequest.of(page, size));
        Page<TransportationBrandsDto> brandsDto = brandsPage.map(brands -> EntityDtoUtils.convertToDto(brands, TransportationBrandsDto.class));

        if (brandsDto.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", brandsDto);
        }
    }

    @GetMapping("customer/transport/find-transport-brand-by-id/{brandId}")
    public ResponseObject findTransportCusById(@PathVariable String brandId) {
        TransportationBrands brands = transportationBrandsService.findByTransportBrandId(brandId);
        TransportationBrandsDto brandsDto = EntityDtoUtils.convertToDto(brands, TransportationBrandsDto.class);

        if (brandsDto == null) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", brandsDto);
        }
    }

    @GetMapping("customer/transport/find-all-transport-schedule/{brandId}")
    public ResponseObject findAllTransportSchedule(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "9") int size,
                                                   @PathVariable String brandId) {
        Page<TransportationSchedules> transportationSchedules = transportationScheduleService.findAllTransportScheduleCus(PageRequest.of(page, size), brandId);
        Page<TransportationSchedulesDto> transportationSchedulesDto = transportationSchedules.map(schedules -> EntityDtoUtils.convertToDto(schedules, TransportationSchedulesDto.class));

        if (transportationSchedulesDto.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", transportationSchedulesDto);
        }
    }

    @GetMapping("customer/transport/find-all-transport-brand-by-filters")
    private ResponseObject findAllTourDetailByFilters(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size,
                                                      @RequestParam(defaultValue = "id") String sortBy,
                                                      @RequestParam(defaultValue = "asc") String sortDir,
                                                      @RequestParam(required = false) BigDecimal price,
                                                      @RequestParam(required = false) String fromLocation,
                                                      @RequestParam(required = false) String toLocation,
                                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date checkInDateFiller,
                                                      @RequestParam(required = false) List<Integer> mediaTypeList,
                                                      @RequestParam(required = false) List<String> listOfVehicleManufacturers,
                                                      @RequestParam(required = false) String searchTerm) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Page<TransportationBrands> brandsPage = transportationBrandsService.findAllCustomerWithFilter(searchTerm, price, fromLocation, toLocation, checkInDateFiller, mediaTypeList, listOfVehicleManufacturers, PageRequest.of(page, size, sort));
        Page<TransportationBrandsDto> brandsDto = brandsPage.map(brands -> EntityDtoUtils.convertToDto(brands, TransportationBrandsDto.class));

        if (brandsDto.isEmpty()) {
            return new ResponseObject("204", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", brandsDto);
        }
    }

    @GetMapping("customer/transport/get-transport-customer-data-list")
    private ResponseObject getAllDataList() {
        Map<String, Object> response = new HashMap<>();

        List<TransportationBrands> transportationBrandsList = transportationBrandsService.findAllCustomerDataList();
        List<TransportationSchedules> transportationSchedulesList = transportationScheduleService.getAllFromLocationAndToLocation();

        List<String> uniqueDataList = transportationBrandsList.stream().flatMap(transportationBrand -> {
            Set<String> brandDetails = new LinkedHashSet<>();
            brandDetails.add(transportationBrand.getAgenciesByAgenciesId().getNameAgency());
            brandDetails.add(transportationBrand.getTransportationBrandName());
            brandDetails.add(transportationBrand.getAgenciesByAgenciesId().getPhone());
            brandDetails.add(transportationBrand.getAgenciesByAgenciesId().getWard());
            brandDetails.add(transportationBrand.getAgenciesByAgenciesId().getDistrict());
            brandDetails.add(transportationBrand.getAgenciesByAgenciesId().getProvince());
            brandDetails.add(transportationBrand.getAgenciesByAgenciesId().getDistrict() + " - " +
                    transportationBrand.getAgenciesByAgenciesId().getProvince());
            brandDetails.add(transportationBrand.getAgenciesByAgenciesId().getWard() + " - " +
                    transportationBrand.getAgenciesByAgenciesId().getDistrict() + " - " +
                    transportationBrand.getAgenciesByAgenciesId().getProvince());
            brandDetails.add(transportationBrand.getAgenciesByAgenciesId().getRepresentativeName());
            transportationBrand.getTransportationsById().forEach(item -> brandDetails.add(item.getTransportationTypesByTransportationTypeId().getTransportationTypeName()));
            return brandDetails.stream();
        }).distinct().collect(Collectors.toList());

        List<String> fromLocationList = transportationSchedulesList.stream().flatMap(ts -> {
            Set<String> transportScheduleDetails = new LinkedHashSet<>();
            transportScheduleDetails.add(ts.getFromLocation());
            return transportScheduleDetails.stream();
        }).distinct().toList();

        List<String> toLocationList = transportationSchedulesList.stream().flatMap(ts -> {
            Set<String> transportScheduleDetails = new LinkedHashSet<>();
            transportScheduleDetails.add(ts.getToLocation());
            return transportScheduleDetails.stream();
        }).distinct().toList();

        response.put("uniqueDataList", uniqueDataList);
        response.put("fromLocationList", fromLocationList);
        response.put("toLocationList", toLocationList);

        if (uniqueDataList.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", response);
        }
    }
}

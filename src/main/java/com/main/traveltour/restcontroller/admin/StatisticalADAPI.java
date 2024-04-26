package com.main.traveltour.restcontroller.admin;

import com.main.traveltour.dto.admin.post.HotelsDto;
import com.main.traveltour.dto.admin.post.TransportationBrandsDto;
import com.main.traveltour.dto.admin.post.VisitLocationsDto;
import com.main.traveltour.dto.admin.type.TransportUtilitiesDto;
import com.main.traveltour.entity.*;
import com.main.traveltour.service.UsersService;
import com.main.traveltour.service.admin.*;
import com.main.traveltour.utils.EntityDtoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1")
public class StatisticalADAPI {

    @Autowired
    UsersService usersService;

    @Autowired
    HotelsServiceAD hotelsServiceAD;

    @Autowired
    VisitLocationsServiceAD visitLocationsServiceAD;

    @Autowired
    TransportationBrandServiceAD transportationBrandServiceAD;

    @Autowired
    private AgencyServiceAD agencyServiceAD;

    @GetMapping("/admin/statistical/compare-amount-user")
    public ResponseObject compareAmountUser() {
        Map<String, Object> response = new HashMap<>();
        Integer nowUser = usersService.countUserNow();
        Integer agoUser = usersService.countUserMonthAgo();
        response.put("nowUser", nowUser);
        response.put("agoUser", agoUser);
        return new ResponseObject("200", "Đã tìm thấy dữ liệu", response);
    }

    @GetMapping("/admin/statistical/compare-amount-agent")
    public ResponseObject compareAmountAgent() {
        Map<String, Object> response = new HashMap<>();
        Integer nowAgent = usersService.countUserAgentNow();
        Integer agoAgent = usersService.countUserAgentMonthAgo();
        response.put("nowAgent", nowAgent);
        response.put("agoAgent", agoAgent);
        return new ResponseObject("200", "Đã tìm thấy dữ liệu", response);
    }

    @GetMapping("/admin/statistical/list-five-agencies")
    public ResponseObject top5AgenciesNearest() {
        List<Agencies> items = agencyServiceAD.findFiveAgenciesNewest();
        return new ResponseObject("200", "Đã tìm thấy dữ liệu", items);
    }

    @GetMapping("/admin/statistical/top-three-hotel")
    public ResponseObject topThreeHotel() {
        List<Hotels> items = hotelsServiceAD.findThreeHotelMostOrder();
        List<HotelsDto> itemDto = EntityDtoUtils.convertToDtoList(items, HotelsDto.class);
        return new ResponseObject("200", "Đã tìm thấy dữ liệu", itemDto);
    }

    @GetMapping("/admin/statistical/top-three-vehicle")
    public ResponseObject topThreeVehicle() {
        List<TransportationBrands> items = transportationBrandServiceAD.findThreeVehicleMostOrder();
        List<TransportationBrandsDto> itemDto = EntityDtoUtils.convertToDtoList(items, TransportationBrandsDto.class);
        return new ResponseObject("200", "Đã tìm thấy dữ liệu", itemDto);
    }

    @GetMapping("/admin/statistical/top-three-visit")
    public ResponseObject topThreeVisit() {
        List<VisitLocations> items = visitLocationsServiceAD.findThreeVisitLocation();
        List<VisitLocationsDto> itemDto = EntityDtoUtils.convertToDtoList(items, VisitLocationsDto.class);
        return new ResponseObject("200", "Đã tìm thấy dữ liệu", itemDto);
    }

}

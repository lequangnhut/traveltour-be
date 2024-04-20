package com.main.traveltour.restcontroller.admin;

import com.main.traveltour.dto.admin.revenue.RevenueDtoAD;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.service.UsersService;
import com.main.traveltour.service.admin.*;
import com.main.traveltour.service.staff.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1")
public class RevenueADAPI {

    @Autowired
    UsersService usersService;
    @Autowired
    HotelServiceService hotelServiceService;
    @Autowired
    TransportationScheduleService transportationScheduleService;
    @Autowired
    VisitLocationService visitLocationService;
    @Autowired
    AgencyServiceAD agencyServiceAD;
    @Autowired
    BookingTourCustomerService bookingTourCustomerService;
    @Autowired
    TourDetailsService tourDetailsService;

    @Autowired
    RevenueServiceAD revenueServiceAD;

    @Autowired
    HotelsServiceAD hotelsServiceAD;

    @Autowired
    VisitLocationsServiceAD visitLocationsServiceAD;

    @Autowired
    TransportationBrandServiceAD transportationBrandServiceAD;


    @GetMapping("/admin/revenue/count-all")
    public ResponseObject countRevenueDashbord() {
        Long findAmountAgencies = 0L;
        Long findAmountUsers = 0L;
        Long findAmountTour = 0L;
        Long findAmountHotels = 0L;
        Long findAmountTrans = 0L;
        Long findAmountVisit = 0L;
        Long findAmountCustomer = 0L;

        findAmountAgencies = agencyServiceAD.countAgency();
        findAmountUsers = usersService.countUsers();
        findAmountTour = tourDetailsService.countTourDetails();
        findAmountHotels = hotelServiceService.countHotels();
        findAmountTrans = transportationScheduleService.countSchedules();
        findAmountVisit = visitLocationService.countVisit();
        findAmountCustomer = bookingTourCustomerService.countCustomer();

        RevenueDtoAD revenueDtoAD = new RevenueDtoAD();
        revenueDtoAD.setAmountAgencies(findAmountAgencies);
        revenueDtoAD.setAmountUsers(findAmountUsers + findAmountCustomer);
        revenueDtoAD.setAmountTour(findAmountTour);
        revenueDtoAD.setAmountHotels(findAmountHotels);
        revenueDtoAD.setAmountTrans(findAmountTrans);
        revenueDtoAD.setAmountVisit(findAmountVisit);


        return new ResponseObject("200", "Đã tìm thấy dữ liệu", revenueDtoAD);
    }

    @GetMapping("/admin/revenue/12-month-of-year")
    public ResponseObject revenueOf12MonthsOfTheYearFromTourBooking(@RequestParam(required = false) Integer year) {

        Map<String, Object> response = new HashMap<>();
        List<BigDecimal> currentYearRevenue = revenueServiceAD.revenueOf12MonthsOfTheYearFromTourBooking(year);
        List<BigDecimal> previousYearIsRevenue = revenueServiceAD.revenueOf12MonthsOfTheYearFromTourBooking(year - 1);

        response.put("currentYearRevenue", currentYearRevenue);
        response.put("previousYearIsRevenue", previousYearIsRevenue);

        if (currentYearRevenue.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", response);
        }
    }

    @GetMapping("/admin/revenue/get-all-year-columns")
    public ResponseObject getAllYearColumn() {
        List<Integer> getAllYear = revenueServiceAD.getAllYearColumn();
        if (getAllYear.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", getAllYear);
        }
    }
    @GetMapping("/admin/revenue/get-all-year-pie")
    public ResponseObject getAllYearPie() {
        List<Integer> getAllYear = revenueServiceAD.getAllYearPie();
        if (getAllYear.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", getAllYear);
        }
    }

    @GetMapping("/admin/revenue/percentage-of-each-type-of-service")
    public ResponseObject percentageOfEachTypeOfService(@RequestParam(required = false) Integer year) {
        Map<String, Double> percentages = new HashMap<>();

        long totalHotels = hotelsServiceAD.countHotelsChart(year);
        long totalVisitLocations = visitLocationsServiceAD.countVisitLocationsChart(year);
        long totalTransportBrands = transportationBrandServiceAD.countTransportationBrandsChart(year);

        long totalServices = totalHotels + totalVisitLocations + totalTransportBrands;

        addPercentageIfNotZero(percentages, "hotelPercentage", totalHotels, totalServices);
        addPercentageIfNotZero(percentages, "visitLocationPercentage", totalVisitLocations, totalServices);
        addPercentageIfNotZero(percentages, "transportBrandPercentage", totalTransportBrands, totalServices);

        if (percentages.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", percentages);
        }
    }

    private void addPercentageIfNotZero(Map<String, Double> percentages, String key, long value, long totalServices) {
        double percentage = totalServices != 0 ? ((double) value / totalServices) * 100 : 0;
        double roundedPercentage = Math.round(percentage * 10) / 10.0;
        percentages.put(key, roundedPercentage);
    }

}

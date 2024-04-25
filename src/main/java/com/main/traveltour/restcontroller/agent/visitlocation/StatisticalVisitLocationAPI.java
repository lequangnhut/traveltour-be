package com.main.traveltour.restcontroller.agent.visitlocation;

import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.service.agent.OrderVisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/agent/visitLocation/statistical/")
public class StatisticalVisitLocationAPI {

    @Autowired
    OrderVisitService orderVisitService;

    @GetMapping("/statisticalBookingVisitLocation")
    public ResponseObject statisticalBookingHotel(@RequestParam(required = false) Integer year,
                                                  @RequestParam(required = false) String visitId) {
        List<Double> response = orderVisitService.getStatisticalBookingVisitLocation(year, visitId);
        if (response.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", response);
        }
    }

    @GetMapping("/statisticsClassifyingAdultAndChildTickets")
    public ResponseObject StatisticsClassifyingAdultAndChildTickets(@RequestParam(required = false) Integer year,
                                                                    @RequestParam(required = false) String visitId) {
        List<Long[]> response = orderVisitService.getNumberOfAdultTickets(year, visitId);
        if (response.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            List<Long> currentYearAdultTickets = new ArrayList<>();
            List<Long> currentYearChildTickets = new ArrayList<>();
            List<Long> previousYearAdultTickets = new ArrayList<>();
            List<Long> previousYearChildTickets = new ArrayList<>();

            for (Long[] ticketCounts : response) {
                currentYearAdultTickets.add(ticketCounts[0]);
                currentYearChildTickets.add(ticketCounts[1]);
                previousYearAdultTickets.add(ticketCounts[2]);
                previousYearChildTickets.add(ticketCounts[3]);
            }

            Map<String, List<Long>> data = new HashMap<>();
            data.put("currentYearAdultTickets", currentYearAdultTickets);
            data.put("currentYearChildTickets", currentYearChildTickets);
            data.put("previousYearAdultTickets", previousYearAdultTickets);
            data.put("previousYearChildTickets", previousYearChildTickets);

            return new ResponseObject("200", "Đã tìm thấy dữ liệu", data);
        }
    }

    @GetMapping("/getRevenueOfTouristAttractions")
    public ResponseObject getRevenueOfTouristAttractions(@RequestParam(required = false) Integer year,
                                                         @RequestParam(required = false) String visitId) {
        List<BigDecimal[]> response = orderVisitService.getRevenueOfTouristAttractions(year, visitId);
        if (response.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            List<BigDecimal> currentYear = new ArrayList<>();
            List<BigDecimal> previousYear = new ArrayList<>();

            for (BigDecimal[] ticketCounts : response) {
                currentYear.add(ticketCounts[0]);
                previousYear.add(ticketCounts[1]);
            }

            Map<String, List<BigDecimal>> data = new HashMap<>();
            data.put("currentYear", currentYear);
            data.put("previousYear", previousYear);

            return new ResponseObject("200", "Đã tìm thấy dữ liệu", data);
        }
    }

    @GetMapping("/getAllOrderVisitYear")
    public ResponseObject getAllYear() {
        List<Integer> getAllYear = orderVisitService.getAllOrderVisitYear();
        if (getAllYear.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", getAllYear);
        }
    }
}

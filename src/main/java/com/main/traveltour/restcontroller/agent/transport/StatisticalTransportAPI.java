package com.main.traveltour.restcontroller.agent.transport;

import com.main.traveltour.dto.agent.hotel.HotelRevenueDto;
import com.main.traveltour.dto.agent.hotel.StatisticalBookingHotelDto;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.service.agent.OrderTransportService;
import com.main.traveltour.service.staff.OrderHotelsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1")
@Validated
public class StatisticalTransportAPI {
    @Autowired
    OrderTransportService orderTransportService;

    @GetMapping("agent/hotel/statistical/findStatisticalBookingTransport")
    public ResponseEntity<List<Double>> findStatisticalBookingTransport(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String id
    ) {
        List<Double> response = orderTransportService.findStatisticalBookingTransport(year, id);
        return ResponseEntity.ok(response);
    }

//    @GetMapping("agent/hotel/statistical/statisticalTransportBrand")
//    public ResponseEntity<List<StatisticalBookingHotelDto>> statisticalTransportBrand(
//            @RequestParam(required = false) Integer year,
//            @RequestParam(required = false) String id
//    ) {
//        List<StatisticalBookingHotelDto> response = orderTransportService.(year, id);
//        return ResponseEntity.ok(null);
//    }

    @GetMapping("agent/hotel/statistical/findTransportRevenueStatistics")
    public ResponseEntity<HotelRevenueDto> getTransportRevenueStatistics(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String id
    ) {
        HotelRevenueDto hotelRevenueDto = orderTransportService.findTransportRevenueStatistics(year, id);
        return ResponseEntity.ok(hotelRevenueDto);
    }

//    @GetMapping("agent/hotel/statistical/findAllOrderHotelYear")
//    public ResponseObject findAllOrderYearPie() {
//        List<Integer> getAllYear = orderTransportService.findAllOrderHotelYear();
//        if (getAllYear.isEmpty()) {
//            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
//        } else {
//            return new ResponseObject("200", "Đã tìm thấy dữ liệu", getAllYear);
//        }
//    }
}

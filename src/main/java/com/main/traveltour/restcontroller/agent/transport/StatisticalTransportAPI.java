package com.main.traveltour.restcontroller.agent.transport;

import com.main.traveltour.dto.agent.hotel.HotelRevenueDto;
import com.main.traveltour.dto.agent.hotel.StatisticalBookingHotelDto;
import com.main.traveltour.dto.agent.transport.StatiscalTransportBrandDto;
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

    @GetMapping("agent/transport/statistical/findStatisticalBookingTransport")
    public ResponseEntity<List<Double>> findStatisticalBookingTransport(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String transportId
    ) {
        List<Double> response = orderTransportService.findStatisticalBookingTransport(year, transportId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("agent/transport/statistical/statisticalTransportBrand")
    public ResponseEntity<List<StatiscalTransportBrandDto>> statisticalTransportBrand(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String transportId
    ) {
        List<StatiscalTransportBrandDto> response = orderTransportService.statisticalTransportBrand(year, transportId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("agent/transport/statistical/findTransportRevenueStatistics")
    public ResponseEntity<HotelRevenueDto> getTransportRevenueStatistics(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String transportId
    ) {
        HotelRevenueDto hotelRevenueDto = orderTransportService.findTransportRevenueStatistics(year, transportId);
        return ResponseEntity.ok(hotelRevenueDto);
    }

    @GetMapping("agent/transport/statistical/findAllOrderTransportYear")
    public ResponseObject findAllOrderTransportYear() {
        List<Integer> getAllYear = orderTransportService.findAllOrderTransportYear();
        if (getAllYear.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", getAllYear);
        }
    }
}

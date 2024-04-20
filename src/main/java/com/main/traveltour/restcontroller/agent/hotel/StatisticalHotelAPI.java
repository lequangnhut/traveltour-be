package com.main.traveltour.restcontroller.agent.hotel;

import com.main.traveltour.dto.agent.hotel.HotelRevenueDto;
import com.main.traveltour.dto.agent.hotel.StatisticalBookingHotelDto;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.service.staff.OrderHotelsService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1")
@Validated
public class StatisticalHotelAPI {
    @Autowired
    OrderHotelsService orderHotelsService;

    @GetMapping("agent/hotel/statistical/statisticalBookingHotel")
    public ResponseEntity<List<Double>> statisticalBookingHotel(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String hotelId
    ) {
        List<Double> response = orderHotelsService.findStatisticalBookingHotel(year, hotelId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("agent/hotel/statistical/statisticalRoomTypeHotel")
    public ResponseEntity<List<StatisticalBookingHotelDto>> statisticalRoomTypeHotel(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String hotelId
    ) {
        List<StatisticalBookingHotelDto> response = orderHotelsService.findStatisticalRoomTypeHotel(year, hotelId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("agent/hotel/statistical/hotelRevenueStatistics")
    public ResponseEntity<HotelRevenueDto> getHotelRevenueStatistics(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String hotelId
    ) {
        HotelRevenueDto hotelRevenueDto = orderHotelsService.findHotelRevenueStatistics(year, hotelId);
        return ResponseEntity.ok(hotelRevenueDto);
    }

    @GetMapping("agent/hotel/statistical/getAllHotelYearPie")
    public ResponseObject getAllYear() {
        List<Integer> getAllYear = orderHotelsService.getAllOrderHotelYear();
        if (getAllYear.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", getAllYear);
        }
    }
}

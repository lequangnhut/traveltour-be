package com.main.traveltour.restcontroller.agent.visitlocation;

import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.service.agent.OrderVisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

}

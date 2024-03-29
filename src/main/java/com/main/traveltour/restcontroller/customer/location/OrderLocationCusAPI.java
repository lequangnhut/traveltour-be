package com.main.traveltour.restcontroller.customer.location;

import com.main.traveltour.entity.OrderVisits;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.service.staff.OrderVisitLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/customer/order-location/")
public class OrderLocationCusAPI {

    @Autowired
    private OrderVisitLocationService orderVisitLocationService;

    @GetMapping("find-by-id/{id}")
    private ResponseObject findOrderLocationById(@PathVariable String id) {
        try {
            OrderVisits orderVisits = orderVisitLocationService.findById(id);

            if (orderVisits == null) {
                return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
            } else {
                return new ResponseObject("200", "Đã tìm thấy dữ liệu", orderVisits);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseObject("500", "Không tìm thấy dữ liệu", e.getMessage());
        }
    }

}

package com.main.traveltour.restcontroller.staff;

import com.main.traveltour.entity.HotelTypes;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.service.staff.HotelTypeServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/staff/hotel-type-service/")
public class HotelTypeServiceAPI {

    @Autowired
    private HotelTypeServiceService hotelTypeServiceService;

    @GetMapping("find-by-id/{id}")
    public ResponseObject findById(@PathVariable int id) {
        Optional<HotelTypes> hotelTypes = Optional.ofNullable(hotelTypeServiceService.findById(id));
        if (hotelTypes.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", hotelTypes);
        }
    }
}

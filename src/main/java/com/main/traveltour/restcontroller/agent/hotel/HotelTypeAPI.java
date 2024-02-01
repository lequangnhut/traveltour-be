package com.main.traveltour.restcontroller.agent.hotel;

import com.main.traveltour.entity.HotelTypes;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.service.agent.HotelsTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1")
public class HotelTypeAPI {

    @Autowired
    private HotelsTypeService hotelsTypeService;

    @GetMapping("agent/hotel-type/list-hotel-type")
    ResponseObject listHotelTypes() {
        List<HotelTypes> listHotelTypes = hotelsTypeService.findAllHotelType();

        if(listHotelTypes.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy loại phòng khách sạn", null);
        }else{
            return new ResponseObject("200", "OK", listHotelTypes);
        }
    }
}

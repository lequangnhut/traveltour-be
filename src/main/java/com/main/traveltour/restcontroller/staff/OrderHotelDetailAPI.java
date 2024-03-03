package com.main.traveltour.restcontroller.staff;

import com.main.traveltour.dto.staff.OrderHotelDetailsDto;
import com.main.traveltour.entity.OrderHotelDetails;
import com.main.traveltour.service.staff.OrderHotelDetailService;
import com.main.traveltour.utils.EntityDtoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/staff/order-hotel-detail")
public class OrderHotelDetailAPI {

    @Autowired
    private OrderHotelDetailService orderHotelDetailService;

    @PostMapping(value = "create-order-hotel-detail")
    public void createOrderHotel(@RequestPart OrderHotelDetailsDto orderHotelDetailsDto) {
        OrderHotelDetails orderHotelDetails = EntityDtoUtils.convertToEntity(orderHotelDetailsDto, OrderHotelDetails.class);
        orderHotelDetailService.save(orderHotelDetails);
    }

}

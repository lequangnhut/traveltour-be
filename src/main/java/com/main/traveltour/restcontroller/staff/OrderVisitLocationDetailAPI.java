package com.main.traveltour.restcontroller.staff;

import com.main.traveltour.dto.staff.OrderVisitDetailsDto;
import com.main.traveltour.entity.OrderVisitDetails;
import com.main.traveltour.service.agent.OrderVisitDetailService;
import com.main.traveltour.utils.EntityDtoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/staff/order-visit-location-detail")
public class OrderVisitLocationDetailAPI {

    @Autowired
    private OrderVisitDetailService orderVisitDetailService;

    @PostMapping(value = "create-order-Visit-detail")
    public void createOrderVisitDetail(@RequestPart("orderVisitDetailsDto") OrderVisitDetailsDto orderVisitDetailsDto) {
        OrderVisitDetails orderVisitDetails = EntityDtoUtils.convertToEntity(orderVisitDetailsDto, OrderVisitDetails.class);
        orderVisitDetailService.save(orderVisitDetails);
    }

}

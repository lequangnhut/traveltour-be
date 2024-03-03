package com.main.traveltour.restcontroller.staff;

import com.main.traveltour.dto.staff.OrderVisitsDto;
import com.main.traveltour.entity.OrderVisits;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.service.staff.staff.OrderVisitLocationService;
import com.main.traveltour.utils.EntityDtoUtils;
import com.main.traveltour.utils.GenerateNextID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/staff/order-visit-location")
public class OrderVisitLocationAPI {

    @Autowired
    private OrderVisitLocationService orderVisitLocationService;

    @PostMapping(value = "create-order-visit-location")
    public ResponseObject createOrderVisit(@RequestPart OrderVisitsDto orderVisitsDto) {
        try {
            String orderVisitId = GenerateNextID.generateNextCode("OVS", orderVisitLocationService.maxCode());
            OrderVisits orderVisits = EntityDtoUtils.convertToEntity(orderVisitsDto, OrderVisits.class);
            orderVisits.setId(orderVisitId);
            orderVisitLocationService.save(orderVisits);

            return new ResponseObject("200", "Thêm mới thành công", orderVisits);
        } catch (Exception e) {
            return new ResponseObject("500", "Thêm mới thất bại", null);
        }
    }

}

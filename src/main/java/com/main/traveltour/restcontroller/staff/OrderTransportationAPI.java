package com.main.traveltour.restcontroller.staff;

import com.main.traveltour.dto.staff.OrderTransportationsDto;
import com.main.traveltour.entity.OrderTransportations;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.entity.TourDetails;
import com.main.traveltour.service.staff.OrderTransportationService;
import com.main.traveltour.service.staff.TourDetailsService;
import com.main.traveltour.utils.EntityDtoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/staff/order-transportation")
public class OrderTransportationAPI {

    @Autowired
    private OrderTransportationService orderTransportationService;

    @Autowired
    private TourDetailsService tourDetailsService;

    @PostMapping(value = "create-order-transportation")
    public ResponseObject createOrderTransportation(@RequestPart("orderTransportationsDto") OrderTransportationsDto orderTransportationsDto, @RequestPart("tourDetailId") String tourDetailId) {
        try {
            OrderTransportations orderTransportations = EntityDtoUtils.convertToEntity(orderTransportationsDto, OrderTransportations.class);

            List<TourDetails> tourDetailsList = new ArrayList<>();
            TourDetails tourDetails = tourDetailsService.findById(tourDetailId);
            tourDetailsList.add(tourDetails);

            orderTransportations.setTourDetails(tourDetailsList);

            orderTransportationService.save(orderTransportations);

            return new ResponseObject("200", "Thêm mới thành công", orderTransportations);
        } catch (Exception e) {
            return new ResponseObject("500", "Thêm mới thất bại", null);
        }
    }

}

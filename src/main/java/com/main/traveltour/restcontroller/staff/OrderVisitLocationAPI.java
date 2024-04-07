package com.main.traveltour.restcontroller.staff;

import com.main.traveltour.dto.staff.OrderVisitsDto;
import com.main.traveltour.entity.OrderVisits;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.entity.TourDetails;
import com.main.traveltour.service.staff.OrderVisitLocationService;
import com.main.traveltour.service.staff.TourDetailsService;
import com.main.traveltour.utils.EntityDtoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/staff/order-visit-location")
public class OrderVisitLocationAPI {

    @Autowired
    private OrderVisitLocationService orderVisitLocationService;

    @Autowired
    private TourDetailsService tourDetailsService;

    @PostMapping(value = "create-order-visit-location")
    public ResponseObject createOrderVisit(@RequestPart OrderVisitsDto orderVisitsDto, @RequestPart String tourDetailId) {
        try {
            OrderVisits orderVisits = EntityDtoUtils.convertToEntity(orderVisitsDto, OrderVisits.class);

            List<TourDetails> tourDetailsList = new ArrayList<>();
            TourDetails tourDetails = tourDetailsService.findById(tourDetailId);
            tourDetailsList.add(tourDetails);

            orderVisits.setTourDetails(tourDetailsList);

            orderVisitLocationService.save(orderVisits);

            return new ResponseObject("200", "Thêm mới thành công", orderVisits);
        } catch (Exception e) {
            return new ResponseObject("500", "Thêm mới thất bại", null);
        }
    }

}

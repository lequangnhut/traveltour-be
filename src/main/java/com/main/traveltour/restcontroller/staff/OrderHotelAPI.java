package com.main.traveltour.restcontroller.staff;

import com.main.traveltour.dto.staff.OrderHotelsDto;
import com.main.traveltour.entity.OrderHotels;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.entity.Roles;
import com.main.traveltour.entity.TourDetails;
import com.main.traveltour.service.staff.OrderHotelsService;
import com.main.traveltour.service.staff.TourDetailsService;
import com.main.traveltour.utils.ChangeCheckInTimeService;
import com.main.traveltour.utils.EntityDtoUtils;
import com.main.traveltour.utils.GenerateNextID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/staff/order-hotel")
public class OrderHotelAPI {

    @Autowired
    private OrderHotelsService orderHotelsService;

    @Autowired
    private TourDetailsService tourDetailsService;

    @Autowired
    private ChangeCheckInTimeService changeCheckInTimeService;

    @PostMapping(value = "create-order-hotel")
    public ResponseObject createOrderHotel(@RequestPart OrderHotelsDto orderHotelsDto, @RequestPart String tourDetailId) {
        try {
            OrderHotels orderHotels = EntityDtoUtils.convertToEntity(orderHotelsDto, OrderHotels.class);

            List<TourDetails> tourDetailsList = new ArrayList<>();
            TourDetails tourDetails = tourDetailsService.findById(tourDetailId);
            tourDetailsList.add(tourDetails);

            orderHotels.setTourDetails(tourDetailsList);
            orderHotels.setCheckIn(changeCheckInTimeService.changeCheckInTime(orderHotels.getCheckIn()));
            orderHotels.setCheckOut(changeCheckInTimeService.changeCheckInTime(orderHotels.getCheckOut()));

            orderHotelsService.save(orderHotels);

            return new ResponseObject("200", "Thêm mới thành công", orderHotels);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseObject("500", "Thêm mới thất bại", null);
        }
    }

}

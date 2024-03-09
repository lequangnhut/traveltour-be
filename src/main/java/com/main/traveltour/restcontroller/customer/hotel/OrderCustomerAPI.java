package com.main.traveltour.restcontroller.customer.hotel;

import com.main.traveltour.dto.customer.hotel.OrderDetailsHotelCustomerDto;
import com.main.traveltour.dto.customer.hotel.OrderHotelCustomerDto;
import com.main.traveltour.dto.staff.OrderHotelDetailsDto;
import com.main.traveltour.dto.staff.OrderHotelsDto;
import com.main.traveltour.entity.OrderHotelDetails;
import com.main.traveltour.entity.OrderHotels;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.service.staff.OrderHotelDetailService;
import com.main.traveltour.service.staff.OrderHotelsService;
import com.main.traveltour.utils.EntityDtoUtils;
import com.main.traveltour.utils.GenerateOrderCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1")
public class OrderCustomerAPI {

    @Autowired
    private OrderHotelsService orderHotelsService;

    @Autowired
    private OrderHotelDetailService orderHotelDetailService;

    @PostMapping("customer/order-hotel/createOrderHotel")
    public ResponseObject createOrderHotel(
            @RequestPart(value = "orderHotel") OrderHotelCustomerDto orderHotel,
            @RequestPart(value = "orderDetailsHotel") List<OrderDetailsHotelCustomerDto> orderDetailsHotel){

        // Thêm hóa đơn khách sạn
        OrderHotels orderHotels = EntityDtoUtils.convertToEntity(orderHotel, OrderHotels.class);
        orderHotelsService.saveOrderHotelCustomer(orderHotels, orderDetailsHotel);

        // Thêm chi tiết hóa đơn khách sạn
        List<OrderHotelDetails> orderHotelDetails = EntityDtoUtils.convertToDtoList(orderDetailsHotel, OrderHotelDetails.class);

        orderHotelDetails.forEach(orderHotelDetail -> {
            orderHotelDetail.setOrderHotelId(orderHotels.getId());
            orderHotelDetailService.saveOrderHotelDetailsCustomer(orderHotelDetail);
        });
        return new ResponseObject("200", "OK", null);
    }
}

package com.main.traveltour.service.staff;

import com.main.traveltour.dto.customer.hotel.OrderDetailsHotelCustomerDto;
import com.main.traveltour.dto.customer.hotel.OrderHotelCustomerDto;
import com.main.traveltour.entity.OrderHotels;

import java.util.List;

public interface OrderHotelsService {
    String maxCodeTourId();

    OrderHotels save(OrderHotels orderHotels);

    void saveOrderHotelCustomer(OrderHotels orderHotels, List<OrderDetailsHotelCustomerDto> orderDetailsHotel);


}

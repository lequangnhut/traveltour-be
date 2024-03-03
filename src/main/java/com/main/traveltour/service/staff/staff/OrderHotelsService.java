package com.main.traveltour.service.staff.staff;

import com.main.traveltour.entity.OrderHotels;

public interface OrderHotelsService {
    String maxCodeTourId();

    OrderHotels save(OrderHotels orderHotels);
}

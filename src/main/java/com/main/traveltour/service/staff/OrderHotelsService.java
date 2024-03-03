package com.main.traveltour.service.staff;

import com.main.traveltour.entity.OrderHotels;

public interface OrderHotelsService {
    String maxCodeTourId();

    OrderHotels save(OrderHotels orderHotels);
}

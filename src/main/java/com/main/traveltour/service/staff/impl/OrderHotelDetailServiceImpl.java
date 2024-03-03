package com.main.traveltour.service.staff.impl;

import com.main.traveltour.entity.OrderHotelDetails;
import com.main.traveltour.repository.OrderHotelDetailsRepository;
import com.main.traveltour.service.staff.OrderHotelDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderHotelDetailServiceImpl implements OrderHotelDetailService {
    @Autowired
    OrderHotelDetailsRepository repo;

    @Override
    public void save(OrderHotelDetails orderHotelDetails) {
        repo.save(orderHotelDetails);
    }
}

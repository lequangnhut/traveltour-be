package com.main.traveltour.service.staff.impl;

import com.main.traveltour.entity.OrderHotels;
import com.main.traveltour.repository.OrderHotelsRepository;
import com.main.traveltour.service.staff.staff.OrderHotelsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderHotelsServiceImpl implements OrderHotelsService {
    @Autowired
    OrderHotelsRepository repo;

    @Override
    public String maxCodeTourId() {
        return repo.maxCodeTourId();
    }

    @Override
    public OrderHotels save(OrderHotels orderHotels) {
        return repo.save(orderHotels);
    }
}

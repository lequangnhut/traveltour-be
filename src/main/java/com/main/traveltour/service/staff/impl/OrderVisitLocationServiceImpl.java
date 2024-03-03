package com.main.traveltour.service.staff.impl;

import com.main.traveltour.entity.OrderHotels;
import com.main.traveltour.entity.OrderVisits;
import com.main.traveltour.repository.OrderHotelsRepository;
import com.main.traveltour.repository.OrderVisitsRepository;
import com.main.traveltour.service.staff.staff.OrderHotelsService;
import com.main.traveltour.service.staff.staff.OrderVisitLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderVisitLocationServiceImpl implements OrderVisitLocationService {
    @Autowired
    OrderVisitsRepository repo;

    @Override
    public String maxCode() {
        return repo.findMaxCode();
    }

    @Override
    public OrderVisits save(OrderVisits orderVisits) {
        return repo.save(orderVisits);
    }
}

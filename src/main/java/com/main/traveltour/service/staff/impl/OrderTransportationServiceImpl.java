package com.main.traveltour.service.staff.impl;

import com.main.traveltour.entity.OrderTransportations;
import com.main.traveltour.repository.OrderTransportationsRepository;
import com.main.traveltour.service.staff.OrderTransportationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderTransportationServiceImpl implements OrderTransportationService {

    @Autowired
    private OrderTransportationsRepository repo;

    @Override
    public String maxCode() {
        return repo.findMaxCode();
    }

    @Override
    public List<OrderTransportations> findAllByTransportationScheduleId(String transportationScheduleId) {
        return repo.findAllByTransportationScheduleId(transportationScheduleId);
    }

    @Override
    public OrderTransportations save(OrderTransportations orderTransportations) {
        return repo.save(orderTransportations);
    }

    @Override
    public void update(OrderTransportations orderTransportations) {
        repo.save(orderTransportations);
    }
}

package com.main.traveltour.service.staff.impl;

import com.main.traveltour.entity.OrderTransportations;
import com.main.traveltour.repository.OrderTransportationsRepository;
import com.main.traveltour.service.staff.OrderTransportationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    @Override
    public Page<OrderTransportations> findByUserIdAndStatus(Integer orderStatus, String email, Pageable pageable) {
        return repo.findAllBookingTransByUserId(orderStatus, email, pageable);
    }

    @Override
    public OrderTransportations findById(String id) {
        return repo.findById(id);
    }

    @Override
    public Optional<OrderTransportations> findByIdOptional(String id) {
        return Optional.ofNullable(repo.findById(id));
    }

}

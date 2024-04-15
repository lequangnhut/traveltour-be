package com.main.traveltour.service.staff.impl;

import com.main.traveltour.entity.OrderVisits;
import com.main.traveltour.repository.OrderVisitsRepository;
import com.main.traveltour.service.staff.OrderVisitLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Override
    public Page<OrderVisits> findByUserIdAndStatus(Integer orderStatus, String email, Pageable pageable) {
        return repo.findAllBookingVisitsByUserId(orderStatus, email, pageable);
    }

    @Override
    public OrderVisits findById(String id) {
        return repo.findById(id);
    }
}

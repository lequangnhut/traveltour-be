package com.main.traveltour.service.agent.Impl;

import com.main.traveltour.entity.OrderVisits;
import com.main.traveltour.repository.OrderVisitsRepository;
import com.main.traveltour.service.agent.OrderVisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OrderVisitServiceImpl implements OrderVisitService {

    @Autowired
    private OrderVisitsRepository orderVisitsRepository;

    @Override
    public String findMaxCode() {
        return orderVisitsRepository.findMaxCode();
    }

    @Override
    public OrderVisits findById(String id) {
        return orderVisitsRepository.findById(id);
    }

    @Override
    public OrderVisits save(OrderVisits orderVisits) {
        return orderVisitsRepository.save(orderVisits);
    }

    @Override
    public Page<OrderVisits> findAllOrderVisits(String brandId, Pageable pageable) {
        return orderVisitsRepository.findAllOrderVisits(brandId, pageable);
    }

    @Override
    public Page<OrderVisits> findAllOrderVisitsWithSearch(String brandId, String searchTerm, Pageable pageable) {
        return orderVisitsRepository.findAllOrderVisitsWithSearch(brandId, searchTerm, pageable);
    }
}

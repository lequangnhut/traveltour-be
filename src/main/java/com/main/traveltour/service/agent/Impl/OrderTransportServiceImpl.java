package com.main.traveltour.service.agent.Impl;

import com.main.traveltour.entity.OrderTransportations;
import com.main.traveltour.repository.OrderTransportationsRepository;
import com.main.traveltour.service.agent.OrderTransportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OrderTransportServiceImpl implements OrderTransportService {

    @Autowired
    private OrderTransportationsRepository repo;

    @Override
    public String findMaxCode() {
        return repo.findMaxCode();
    }

    @Override
    public OrderTransportations findById(String id) {
        return repo.findById(id);
    }

    @Override
    public OrderTransportations save(OrderTransportations orderTransportations) {
        return repo.save(orderTransportations);
    }

    @Override
    public Page<OrderTransportations> findAllOrderTransportAgent(String transportBrandId, String scheduleId, Pageable pageable) {
        return repo.findAllOrderTransportAgent(transportBrandId, scheduleId, pageable);
    }

    @Override
    public Page<OrderTransportations> findAllOrderTransportAgentWithSearch(String transportBrandId, String scheduleId, String searchTerm, Pageable pageable) {
        return repo.findAllOrderTransportAgentWithSearch(transportBrandId, scheduleId, searchTerm, pageable);
    }
}

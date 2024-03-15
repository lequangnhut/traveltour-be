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
    private OrderTransportationsRepository orderTransportationsRepository;

    @Override
    public String findMaxCode() {
        return orderTransportationsRepository.findMaxCode();
    }

    @Override
    public OrderTransportations findById(String id) {
        return orderTransportationsRepository.findById(id);
    }

    @Override
    public OrderTransportations save(OrderTransportations orderTransportations) {
        return orderTransportationsRepository.save(orderTransportations);
    }

    @Override
    public Page<OrderTransportations> findAllOrderTransport(String transportBrandId, Pageable pageable) {
        return orderTransportationsRepository.findAllOrderTransport(transportBrandId, pageable);
    }

    @Override
    public Page<OrderTransportations> findAllOrderTransportWithSearch(String transportBrandId, String searchTerm, Pageable pageable) {
        return orderTransportationsRepository.findAllOrderTransportWithSearch(transportBrandId, searchTerm, pageable);
    }
}

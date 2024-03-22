package com.main.traveltour.service.agent.Impl;

import com.main.traveltour.entity.OrderTransportationDetails;
import com.main.traveltour.repository.OrderTransportationDetailsRepository;
import com.main.traveltour.service.agent.OrderTransportDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderTransportDetailServiceImpl implements OrderTransportDetailService {

    @Autowired
    private OrderTransportationDetailsRepository repo;

    @Override
    public OrderTransportationDetails save(OrderTransportationDetails orderTransportationDetails) {
        return repo.save(orderTransportationDetails);
    }
}
